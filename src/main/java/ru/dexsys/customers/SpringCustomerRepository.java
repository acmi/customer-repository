package ru.dexsys.customers;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {

    private JdbcTemplate jdbc;

    public SpringCustomerRepository(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Customer> findById(String id) {
        String queryFindByIdCustomer = "SELECT customer.id AS customer_id, customer.name, " +
                "customer_contact.id AS contact_id, customer_contact.contact, customer_contact.type " +
                "FROM customer " +
                "LEFT OUTER JOIN customer_contact ON customer.id = customer_contact.customer_id " +
                "WHERE customer.id = ?";
        List<Customer> customers = jdbc.query(queryFindByIdCustomer, new CustomerExtractor(), id);
        if (customers != null && customers.size() == 1) {
            return Optional.of(customers.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        String queryFindAllCustomers = "SELECT customer.id AS customer_id, customer.name, " +
                "customer_contact.id AS contact_id, customer_contact.contact, customer_contact.type " +
                "FROM customer " +
                "LEFT OUTER JOIN customer_contact ON customer.id = customer_contact.customer_id";
        return jdbc.query(queryFindAllCustomers, new CustomerExtractor());
    }

    @Override
    public Customer save(Customer entity) {
        String querySaveCustomer = "INSERT INTO customer (id, name) VALUES (?, ?)";
        String querySaveCustomerContact = "INSERT INTO customer_contact (id, contact, type, customer_id) VALUES (?, ?, ?, ?)";
        jdbc.update(querySaveCustomer, entity.getId(), entity.getName());
        if (entity.getContacts() != null && !entity.getContacts().isEmpty()) {
            for (CustomerContact contact : entity.getContacts()) {
                jdbc.update(querySaveCustomerContact, contact.getId(), contact.getContact(), contact.getType().toString(), entity.getId());
            }
        }
        return entity;
    }

    @Override
    public void update(Customer entity) {
        String queryUpdateCustomer = "UPDATE customer SET name = ? WHERE id = ?";
        String queryUpdateCustomerContacts = "UPDATE customer_contact SET contact = ?, type = ? WHERE id = ?";
        jdbc.update(queryUpdateCustomer, entity.getName(), entity.getId());
        if (entity.getContacts() != null && !entity.getContacts().isEmpty()) {
            for (CustomerContact contact : entity.getContacts()) {
                jdbc.update(queryUpdateCustomerContacts, contact.getContact(), contact.getType().toString(), contact.getId());
            }
        }
    }

    @Override
    public void deleteById(String id) {
        String queryDeleteByIdCustomerContacts = "DELETE FROM customer_contact WHERE customer_id = ?";
        String queryDeleteByIdCustomer = "DELETE FROM customer WHERE id = ?";
        jdbc.update(queryDeleteByIdCustomerContacts, id);
        jdbc.update(queryDeleteByIdCustomer, id);
    }
}
