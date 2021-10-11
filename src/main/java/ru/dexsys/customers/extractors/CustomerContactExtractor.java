package ru.dexsys.customers.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.dexsys.customers.entities.Customer;
import ru.dexsys.customers.entities.CustomerContact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerContactExtractor implements ResultSetExtractor<List<CustomerContact>> {
    @Override
    public List<CustomerContact> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<CustomerContact> contacts = new ArrayList<>();
        while (resultSet.next()) {
            CustomerContact contact = new CustomerContact();
            contact.setId(resultSet.getString("contact_id"));
            contact.setContact(resultSet.getString("contact"));
            if (resultSet.getString("type").equalsIgnoreCase("email")) {
                contact.setType(CustomerContact.Type.EMAIL);
            } else {
                contact.setType(CustomerContact.Type.MOBILE);
            }
            contacts.add(contact);
        }
        return contacts;
    }
}
