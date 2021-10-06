package ru.dexsys.customers;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerRepository implements CustomerRepository {
    public SpringCustomerRepository(DataSource dataSource) {
    }

    @Override
    public Optional<Customer> findById(String id) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
    }

    @Override
    public List<Customer> findAll() {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
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
