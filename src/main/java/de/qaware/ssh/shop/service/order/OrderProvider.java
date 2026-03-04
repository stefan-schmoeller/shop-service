package de.qaware.ssh.shop.service.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ApplicationScoped
public class OrderProvider {
    
    private static final String PUT_ORDER_QUERY = "INSERT INTO orders (orderId, productId, receiptKey) VALUES (?, ?, ?)";
    private static final String GET_ORDER_QUERY = "SELECT * FROM orders WHERE orderId = ?";
    
    @Inject
    DataSource dataSource;
    
    public void persist(Order order) {
        if (order == null) {
            return;
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(PUT_ORDER_QUERY)
        ) {
            statement.setString(1, order.orderId().toString());
            statement.setInt(2, order.productId());
            statement.setString(3, order.receiptKey());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException("Error in communication with orders database", e);
        }
    }
    
    public Order getOrder(UUID orderId) {
        if (orderId == null) {
            return null;
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ORDER_QUERY)
        ) {
            statement.setString(1, orderId.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Order(
                        UUID.fromString(result.getString("orderId")),
                        result.getInt("productId"),
                        result.getString("receiptKey")
                );
            }
        } catch (SQLException e) {
            throw new InternalServerErrorException("Error in communication with orders database", e);
        }
        return null;
    }
    
}
