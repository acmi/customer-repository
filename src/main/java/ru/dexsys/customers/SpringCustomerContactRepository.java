package ru.dexsys.customers;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpringCustomerContactRepository implements CustomerContactRepository {
    private final JdbcOperations jdbc;
    private final CustomerRepository customerRepository;

    public SpringCustomerContactRepository(DataSource dataSource,
                                           CustomerRepository customerRepository) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<CustomerContact> findById(String id) {
        return jdbc.queryForList("select customer_id from customer_contact where id=?", String.class, id)
                .stream()
                .map(customerRepository::findById)
                .flatMap(Optional::stream)
                .map(Customer::getContacts)
                .flatMap(Collection::stream)
                .filter(contact -> contact.getId().equals(id))
                .findAny();
    }

    @Override
    public List<CustomerContact> findAll() {
        return customerRepository.findAll().stream()
                .map(Customer::getContacts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerContact save(CustomerContact entity) {
        jdbc.update("insert into customer_contact (id, type, contact, customer_id) values (?, ?, ?, ?)",
                entity.getId(),
                entity.getType().name(),
                entity.getContact(),
                entity.getUser().getId());
        return entity;
    }

    @Override
    public void update(CustomerContact entity) {
        jdbc.update("update customer_contact set type=?, contact=? where id=?",
                entity.getType().name(),
                entity.getContact(),
                entity.getId());
    }

    @Override
    public void deleteById(String id) {
        jdbc.update("delete from customer_contact where id=?", id);
    }
}
