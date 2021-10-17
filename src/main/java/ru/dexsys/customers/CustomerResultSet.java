package ru.dexsys.customers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerResultSet implements ResultSetExtractor<List<Customer>> {
    @Override
    public List<Customer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Customer> users = new ArrayList<>();
        Customer user = null;
        while (resultSet.next()) {
            if (user == null) {
                user = new Customer();
                user.setId(resultSet.getString("customer_id"));
                user.setName(resultSet.getString("name"));
                user.setContacts(new ArrayList<>());
                users.add(user);
            }
            if (resultSet.getString("contact_id") != null) {
                CustomerContact customerContact = new CustomerContact();
                customerContact.setId(resultSet.getString("contact_id"));
                customerContact.setContact(resultSet.getString("contact"));
                customerContact.setUser(user);
                if (resultSet.getString("type").contains(CustomerContact.Type.EMAIL.toString())) {
                    customerContact.setType(CustomerContact.Type.EMAIL);
                } else {
                    customerContact.setType(CustomerContact.Type.MOBILE);
                }
                user.addContact(customerContact);
            }

        }
        return users;
    }
}




