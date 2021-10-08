package ru.dexsys.customers;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public SpringCustomerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Customer> findById(String id) {
        Customer customer = new Customer();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from CUSTOMER where id = ?")) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        customer.setId(resultSet.getString("ID"));
                        customer.setName(resultSet.getString("NAME"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.of(customer);
    }

    @Override
    public List<Customer> findAll() {
        Customer customer = new Customer();
        List<Customer> customerList = null;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from CUSTOMER")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        customer.setId(resultSet.getString("ID"));
                        customer.setName(resultSet.getString("NAME"));
                        customerList.add(customer);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    @Override
    public Customer save(Customer entity) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
    }

    @Override
    public void update(Customer entity) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
    }

    @Override
    public void deleteById(String id) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
    }
}
