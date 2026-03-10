package de.qaware.ssh.shop.service.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ProductProvider {

    private static final String PRODUCTS_QUERY = "SELECT * FROM products WHERE id =ANY (?)";

    @Inject
    DataSource dataSource;

    public List<Product> getProducts(Set<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(PRODUCTS_QUERY)
        ) {
            statement.setArray(1, connection.createArrayOf("integer", ids.toArray()));
            ResultSet result = statement.executeQuery();

            List<Product> products = new ArrayList<>();
            while (result.next()) {
                Product product = new Product(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getDouble("price")
                );
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Error in communication with products database", e);
        }
    }

}
