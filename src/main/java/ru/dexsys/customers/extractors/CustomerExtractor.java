package ru.dexsys.customers.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.dexsys.customers.entities.Customer;
import ru.dexsys.customers.entities.CustomerContact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerExtractor implements ResultSetExtractor<List<Customer>> {

    @Override
    public List<Customer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Customer> customers = new ArrayList<>();
        Customer customer = null;
        while (resultSet.next()) {
            if (customer == null || !customer.getId().equalsIgnoreCase(resultSet.getString("customer_id"))) {
                customer = new Customer();
                customer.setId(resultSet.getString("customer_id"));
                customer.setName(resultSet.getString("name"));
                customer.setContacts(new ArrayList<>());
                customers.add(customer);
            }
            if (resultSet.getString("contact_id") == null) {
                continue;
            }
            CustomerContact contact = new CustomerContact();
            contact.setId(resultSet.getString("contact_id"));
            contact.setContact(resultSet.getString("contact"));
            contact.setUser(customer);
            if (resultSet.getString("type").equalsIgnoreCase("email")) {
                contact.setType(CustomerContact.Type.EMAIL);
            } else {
                contact.setType(CustomerContact.Type.MOBILE);
            }
            customer.addContact(contact);
        }
        return customers;
    }
}
