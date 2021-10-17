package ru.dexsys.customers;


import org.springframework.jdbc.core.namedparam.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerContactRepository implements CustomerContactRepository {
    private final NamedParameterJdbcOperations jdbc;

    public SpringCustomerContactRepository(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<CustomerContact> findById(String id) {
        return jdbc.query("select customer_contact.id as contact_id, customer_contact.contact, " +
                "customer_contact.type, customer_contact.customer_id, customer.name " +
                "from customer_contact " +
                "left outo join CUSTOMER on customer_contact.customer_id = customer.id " +
                "where customer_contact.id = :id", new MapSqlParameterSource("id", id), new CustomerContactResultSetExctractor()).stream().findAny();
    }

    @Override
    public List<CustomerContact> findAll() {
        return jdbc.query("select id, name from CUSTOMER", new CustomerContactResultSetExctractor());
    }

    @Override
    public CustomerContact save(CustomerContact entity) {
        jdbc.update("insert into customer_contact (id, contact, type, customer_id) VALUES (:id, :contact, :type, :customer_id)", new BeanPropertySqlParameterSource(entity));
        return entity;
    }

    @Override
    public void update(CustomerContact entity) {
        jdbc.update("update customer_contact set contact = :contact, type = :type where id = :id", new BeanPropertySqlParameterSource(entity));
    }

    @Override
    public void deleteById(String id) {
        jdbc.update("delete from customer_contact where id = :id", new MapSqlParameterSource("id", id));

    }
}
