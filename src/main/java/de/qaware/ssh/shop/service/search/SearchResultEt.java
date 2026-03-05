package de.qaware.ssh.shop.service.search;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

@Data
public final class SearchResultEt {
    
    @Field("product_id")
    private int id;
    @Field("description_de")
    private String germanDescription;
    @Field("description_en")
    private String englishDescription;
    
}
