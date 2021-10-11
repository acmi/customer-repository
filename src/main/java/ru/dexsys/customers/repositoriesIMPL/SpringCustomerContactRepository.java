package ru.dexsys.customers.repositoriesIMPL;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.dexsys.customers.entities.CustomerContact;
import ru.dexsys.customers.extractors.CustomerContactExtractor;
import ru.dexsys.customers.repositoriesAPI.CustomerContactRepository;
import ru.dexsys.customers.repositoriesAPI.CustomerRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerContactRepository implements CustomerContactRepository {

    private JdbcTemplate jdbcTemplate;
    private CustomerRepository customerRepository;

    public SpringCustomerContactRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        customerRepository = new SpringCustomerRepository(dataSource);
    }

    @Override
    public Optional<CustomerContact> findById(String id) {
        String queryFindByIdCustomerContact = "SELECT customer_contact.id AS contact_id, customer_contact.contact, " +
                "customer_contact.type, customer_contact.customer_id, customer.name " +
                "FROM customer_contact " +
                "LEFT OUTER JOIN customer ON customer_contact.customer_id = customer.id " +
                "WHERE customer_contact.id = ?";
        List<CustomerContact> contacts = jdbcTemplate.query(queryFindByIdCustomerContact, new CustomerContactExtractor(), id);
        if (contacts != null && contacts.size() == 1) {
            return Optional.of(contacts.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<CustomerContact> findAll() {
        String queryFindAllCustomerContacts = "SELECT customer_contact.id AS contact_id, customer_contact.contact, " +
                "customer_contact.type, customer_contact.customer_id, customer.name " +
                "FROM customer_contact " +
                "LEFT OUTER JOIN customer ON customer_contact.customer_id = customer.id";
        return jdbcTemplate.query(queryFindAllCustomerContacts, new CustomerContactExtractor());
    }

    @Override
    public CustomerContact save(CustomerContact entity) {
        String saveCustomerContactQuery = "INSERT INTO customer_contact (id, contact, type, customer_id) VALUES (?, ?, ?, ?)";
        if (customerRepository.findById(entity.getUser().getId()).isEmpty()) {
            String querySaveCustomer = "INSERT INTO customer (id, name) VALUES (?, ?)";
            jdbcTemplate.update(querySaveCustomer, entity.getUser().getId(), entity.getUser().getName());
        }
        jdbcTemplate.update(saveCustomerContactQuery, entity.getId(), entity.getContact(), entity.getType().toString(), entity.getUser().getId());
        return entity;
    }

    @Override
    public void update(CustomerContact entity) {
        String updateCustomerContactQuery = "UPDATE customer_contact SET contact = ?, type = ? WHERE id = ?";
        jdbcTemplate.update(updateCustomerContactQuery, entity.getContact(), entity.getType().toString(), entity.getId());
    }

    @Override
    public void deleteById(String id) {
        String deleteByIdContactQuery = "DELETE FROM customer_contact WHERE id = ?";
        jdbcTemplate.update(deleteByIdContactQuery, id);
    }
}
