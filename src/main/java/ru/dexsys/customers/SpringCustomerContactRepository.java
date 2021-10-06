package ru.dexsys.customers;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SpringCustomerContactRepository implements CustomerContactRepository {
    public SpringCustomerContactRepository(DataSource dataSource) {
    }

    @Override
    public Optional<CustomerContact> findById(String id) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED"); //TODO
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
}
