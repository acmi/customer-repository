package ru.dexsys.customers.repositoriesIMPL;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.dexsys.customers.entities.Customer;
import ru.dexsys.customers.entities.CustomerContact;
import ru.dexsys.customers.extractors.CustomerExtractor;
import ru.dexsys.customers.repositoriesAPI.CustomerRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {

    private JdbcTemplate jdbcTemplate;

    public SpringCustomerRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Customer> findById(String id) {
        String findCustomerByIdQuery = "SELECT customer.id AS customer_id, customer.name, " +
                "customer_contact.id AS contact_id, customer_contact.contact, customer_contact.type " +
                "FROM customer " +
                "LEFT OUTER JOIN customer_contact ON customer.id = customer_contact.customer_id " +
                "WHERE customer.id = ?";
        List<Customer> customers = jdbcTemplate.query(findCustomerByIdQuery, new CustomerExtractor(), id);
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
        return jdbcTemplate.query(queryFindAllCustomers, new CustomerExtractor());
    }

    @Override
    public Customer save(Customer entity) {
        String querySaveCustomer = "INSERT INTO customer (id, name) VALUES (?, ?)";
        String querySaveCustomerContact = "INSERT INTO customer_contact (id, contact, type, customer_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(querySaveCustomer, entity.getId(), entity.getName());
        if (entity.getContacts() != null && !entity.getContacts().isEmpty()) {
            for (CustomerContact contact : entity.getContacts()) {
                jdbcTemplate.update(querySaveCustomerContact, contact.getId(), contact.getContact(), contact.getType().toString(), entity.getId());
            }
        }
        return entity;
    }

    @Override
    public void update(Customer entity) {
        String updateCustomerQuery = "UPDATE customer SET name = ? WHERE id = ?";
        jdbcTemplate.update(updateCustomerQuery, entity.getName(), entity.getId());
    }

    @Override
    public void deleteById(String id) {
        String deleteByIdCustomerQuery = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(deleteByIdCustomerQuery, id);
    }
}
