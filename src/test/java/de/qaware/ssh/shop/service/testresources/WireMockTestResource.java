package de.qaware.ssh.shop.service.testresources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.Sets;
import de.qaware.ssh.shop.service.inventory.InventoryEntry;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.havingExactly;
import static com.github.tomakehurst.wiremock.client.WireMock.including;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();

        createMappings();

        Map<String, String> configuration = new HashMap<>();
        configuration.put("quarkus.rest-client.inventory-service.url", wireMockServer.baseUrl());

        return configuration;
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private void createMappings() {
        Map<Integer, Integer> stock = Map.of(
                1, 10,
                2, 5,
                3, 7,
                4, 0,
                5, 23
        );
        for (Map.Entry<Integer, Integer> stockEntry : stock.entrySet()) {
            wireMockServer.addStubMapping(
                    stubFor(
                            get(urlPathTemplate("/inventory/{id}"))
                                    .withPathParam("id", equalTo(stockEntry.getKey().toString()))
                                    .atPriority(1)
                                    .willReturn(okForJson(new InventoryEntry(stockEntry.getKey(), stockEntry.getValue())))
                    )
            );
        }
        wireMockServer.addStubMapping(
                stubFor(
                        get(urlPathTemplate("/inventory/{id}"))
                                .withPathParam("id", equalTo("" + Integer.MAX_VALUE))
                                .atPriority(1)
                                .willReturn(ok().withFixedDelay(2100))
                )
        );
        wireMockServer.addStubMapping(
                stubFor(
                        get(urlPathTemplate("/inventory/{id}"))
                                .atPriority(2)
                                .willReturn(badRequest())
                )
        );

        for (Set<Integer> ids : Sets.powerSet(stock.keySet())) {
            wireMockServer.addStubMapping(
                    stubFor(
                            get(urlPathEqualTo("/inventory"))
                                    .withQueryParam("id", havingExactly(ids.stream().map(Object::toString).toArray(String[]::new)))
                                    .atPriority(1)
                                    .willReturn(
                                            okForJson(
                                                    ids.stream().map(id -> new InventoryEntry(id, stock.get(id))).toList()
                                            )
                                    )
                    )
            );
        }
        wireMockServer.addStubMapping(
                stubFor(
                        get(urlPathEqualTo("/inventory"))
                                .withQueryParam("id", including("" + Integer.MAX_VALUE))
                                .atPriority(1)
                                .willReturn(ok().withFixedDelay(2100))
                )
        );
        wireMockServer.addStubMapping(
                stubFor(
                        get(urlPathEqualTo("/inventory"))
                                .atPriority(2)
                                .willReturn(badRequest())
                )
        );
    }

}
