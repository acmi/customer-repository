package ru.dexsys.customers;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SpringCustomerContactRepository implements CustomerContactRepository {
  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;

  public SpringCustomerContactRepository(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Optional<CustomerContact> findById(String id) {
    CustomerContact customerContact = new CustomerContact();
    Customer customer = new Customer();
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement("select * from CUSTOMER_CONTACT 'cc' join CUSTOMER c on (cc.CUSTOMER_ID = c.ID) where cc.id = ?")) {
        statement.setString(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            customerContact.setId(resultSet.getString("ID"));
            customerContact.setContact(resultSet.getString("CONTACT"));
            customerContact.setType(CustomerContact.Type.valueOf(resultSet.getString("TYPE")));
            customer.setId(resultSet.getString("ID"));
            customer.setName(resultSet.getString("NAME"));
            customerContact.setUser(findCustomer(resultSet.getString("CUSTOMER_ID")));
          }
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return Optional.of(customerContact);
  }

  @Override
  public List<CustomerContact> findAll() {
    throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
  }

  @Override
  public CustomerContact save(CustomerContact entity) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
  }

  @Override
  public void update(CustomerContact entity) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
  }

  @Override
  public void deleteById(String id) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
  }

  private Customer findCustomer(String id) throws SQLException {
    Customer customer = new Customer();
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement("select * from CUSTOMER where id = ?")) {
        statement.setString(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            customer.setId(resultSet.getString("ID"));
            customer.setName(resultSet.getString("NAME"));
          }
        }
      }
      return customer;
    }
  }
}
