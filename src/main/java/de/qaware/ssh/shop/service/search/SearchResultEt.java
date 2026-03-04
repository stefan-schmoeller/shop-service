package de.qaware.ssh.shop.service.search;

import org.apache.solr.client.solrj.beans.Field;

public record SearchResultEt(
        @Field("id") int id,
        @Field("description_de") String germanDescription,
        @Field("description_en") String englishDescription
) {
}
