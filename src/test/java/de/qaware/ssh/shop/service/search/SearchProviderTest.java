package de.qaware.ssh.shop.service.search;

import de.qaware.ssh.shop.service.testresources.WithSolrTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithSolrTestResource(collections = {"products"}, logConsumeEnabled = true)
class SearchProviderTest {

    @Inject
    SearchProvider searchProvider;

    @Test
    void searchExistingProduct() {
        List<Integer> searchResult = searchProvider.query("Keyboard");
        assertThat(searchResult).singleElement().isEqualTo(3);
    }

    @Test
    void searchNonExistingProduct() {
        List<Integer> searchResult = searchProvider.query("Disk");
        assertThat(searchResult).isEmpty();
    }

}
