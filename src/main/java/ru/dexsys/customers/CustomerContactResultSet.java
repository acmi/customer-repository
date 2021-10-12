package ru.dexsys.customers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerContactResultSet implements ResultSetExtractor<List<CustomerContact>> {
  @Override
  public List<CustomerContact> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    List<CustomerContact> customerContacts = new ArrayList<>();
    Customer user = null;
    while (resultSet.next()) {
      if (user == null) {
        user = new Customer();
        user.setId(resultSet.getString("customer_id"));
        user.setName("name");
      }
      CustomerContact customerContact = new CustomerContact();
      customerContact.setId(resultSet.getString("contact_id"));
      customerContact.setContact(resultSet.getString("contact"));
      for (CustomerContact.Type b : CustomerContact.Type.values()) {
        if (resultSet.getString("type").contains("email")) {
          customerContact.setType(CustomerContact.Type.EMAIL);
        } else {
          customerContact.setType(CustomerContact.Type.MOBILE);
        }
      }
      user.addContact(customerContact);
      customerContacts.add(customerContact);
    }
    return customerContacts;
  }
}
