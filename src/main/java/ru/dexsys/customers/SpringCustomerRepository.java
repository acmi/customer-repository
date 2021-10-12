package ru.dexsys.customers;

import org.springframework.jdbc.core.namedparam.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {
  private final NamedParameterJdbcOperations jdbc;

  public SpringCustomerRepository(DataSource dataSource) {
    this.jdbc = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Optional<Customer> findById(String id) {
    return jdbc.query("select customer.id AS customer_id, customer.name, " +
        "customer_contact.id AS contact_id, customer_contact.contact, customer_contact.type " +
        "from CUSTOMER " +
        "LEFT OUTER JOIN CUSTOMER_CONTACT ON customer.id = customer_contact.customer_id " +
        "where customer.id = :id", new MapSqlParameterSource("id", id), new CustomerResultSet()).stream().findAny();
  }

  @Override
  public List<Customer> findAll() {
    return jdbc.query("select id, name from CUSTOMER", new CustomerResultSet());
  }

  @Override
  public Customer save(Customer entity) {
    jdbc.update("insert into customer (id, name) values (:id, :name)",
        new BeanPropertySqlParameterSource(entity));
    if (entity.getContacts() != null) {
      for (CustomerContact contact : entity.getContacts()) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", contact.getId());
        parameterSource.addValue("contact", contact.getContact());
        parameterSource.addValue("type", contact.getType().toString());
        parameterSource.addValue("customer_id", entity.getId());
        jdbc.update("INSERT INTO customer_contact (id, contact, type, customer_id) VALUES (:id, :contact, :type, :customer_id)",
            parameterSource);
      }
    }
    return entity;
  }

  @Override
  public void update(Customer entity) {
    SqlParameterSource namedParameters = new MapSqlParameterSource().
        addValue("id", entity.getId()).addValue("id", entity.getName());
    jdbc.update("update customer set name = :name where id = :id", namedParameters);
  }

  @Override
  public void deleteById(String id) {
    jdbc.update("delete from customer where id = :id", new MapSqlParameterSource("id", id));
  }
}
