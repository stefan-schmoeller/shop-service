package de.qaware.ssh.shop.service.search;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class SearchProvider {
    
    public List<Integer> query(String searchTerm) {
        return Collections.emptyList();
    }
    
}
