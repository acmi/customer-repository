package ru.dexsys.customers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerContactExtractor implements ResultSetExtractor<List<CustomerContact>> {

    @Override
    public List<CustomerContact> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<CustomerContact> contacts = new ArrayList<>();
        Customer customer = null;
        while (resultSet.next()) {
            if (customer == null || !resultSet.getString("customer_id").equalsIgnoreCase(customer.getId())) {
                customer = new Customer();
                customer.setId(resultSet.getString("customer_id"));
                customer.setName(resultSet.getString("name"));
            }
            CustomerContact contact = new CustomerContact();
            contact.setId(resultSet.getString("contact_id"));
            contact.setContact(resultSet.getString("contact"));
            if (resultSet.getString("type").equalsIgnoreCase("email")) {
                contact.setType(CustomerContact.Type.EMAIL);
            } else {
                contact.setType(CustomerContact.Type.MOBILE);
            }
            customer.addContact(contact);
            contacts.add(contact);
        }
        return contacts;
    }
}
