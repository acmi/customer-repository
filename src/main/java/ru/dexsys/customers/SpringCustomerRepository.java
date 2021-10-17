package ru.dexsys.customers;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public SpringCustomerRepository(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return jdbc.query(
                "select customer.id as customer_id, customer.name, " +
                        "customer_contact.id as contact_id," +
                        "customer_contact.contact," +
                        "customer_contact.type " +
                        "from customer " +
                        "left outer join customer_contact on customer.id = customer_contact.customer_id " +
                        "where customer.id = :id",
                new MapSqlParameterSource("id", id),
                new CustomerResultSet()).stream().findAny();
    }

    @Override
    public List<Customer> findAll() {
        return jdbc.query("select id, name from customer", new CustomerResultSet());
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
                jdbc.update("insert into customer_contact (id, contact, type, customer_id) values (:id, :contact, :type, :customer_id)",
                        parameterSource);
            }
        }
        return entity;
    }

    @Override
    public void update(Customer entity) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().
                addValue("id", entity.getId()).addValue("id", entity.getName());
        jdbc.update("update customer set name = :name where id = :id", sqlParameterSource);
    }

    @Override
    public void deleteById(String id) {
        jdbc.update("delete from customer where id = :id", new MapSqlParameterSource("id", id));
    }
}
