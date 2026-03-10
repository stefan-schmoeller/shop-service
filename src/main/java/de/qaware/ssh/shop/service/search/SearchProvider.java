package de.qaware.ssh.shop.service.search;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class SearchProvider {

    @Inject
    @ConfigProperty(name = "solr.base.url")
    String solrBaseUrl;

    private SolrClient solrClient;

    @PostConstruct
    void init() {
        solrClient = new Http2SolrClient.Builder(solrBaseUrl).withDefaultCollection("products").build();
    }

    public List<Integer> query(String searchTerm) {
        SolrQuery query = new SolrQuery("description_de:*" + searchTerm + "* OR description_en:*" + searchTerm + "*");

        QueryResponse searchResult;

        try {
            searchResult = solrClient.query(query);
        } catch (SolrServerException | IOException e) {
            throw new InternalServerErrorException("Error calling Solr.", e);
        }

        return searchResult.getBeans(SearchResultEt.class).stream().map(SearchResultEt::getId).toList();
    }

}
