package ru.dexsys.customers;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpringCustomerRepository implements CustomerRepository {
    private final JdbcOperations jdbc;

    public SpringCustomerRepository(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(jdbc.query(
                "select" +
                        "        customer.id as cust_id," +
                        "        customer.name as cust_name," +
                        "        contacts.id as cont_id," +
                        "        contacts.type as cont_type," +
                        "        contacts.contact as cont_contact " +
                        "    from" +
                        "        customer customer " +
                        "    left outer join" +
                        "        customer_contact contacts " +
                        "            on customer.id=contacts.customer_id " +
                        "    where" +
                        "        customer.id=?",
                rs -> {
                    Customer customer = null;
                    while (rs.next()) {
                        if (customer == null) {
                            customer = new Customer();
                            customer.setId(rs.getString("cust_id"));
                            customer.setName(rs.getString("cust_name"));
                        }
                        if (rs.getString("cont_id") != null) {
                            customer.addContact(
                                    new CustomerContact(
                                            rs.getString("cont_id"),
                                            CustomerContact.Type.valueOf(rs.getString("cont_type")),
                                            rs.getString("cont_contact"),
                                            customer
                                    )
                            );
                        }
                    }
                    return customer;
                },
                id));
    }

    @Override
    public List<Customer> findAll() {
        return jdbc.query(
                "select" +
                        "        customer.id as cust_id," +
                        "        customer.name as cust_name," +
                        "        contacts.id as cont_id," +
                        "        contacts.contact as cont_contact," +
                        "        contacts.type as cont_type " +
                        "    from" +
                        "        customer customer " +
                        "    left outer join" +
                        "        customer_contact contacts " +
                        "            on customer.id=contacts.customer_id ",
                (ResultSetExtractor<List<Customer>>) rs -> {
                    Map<String, Customer> customers = new LinkedHashMap<>();
                    while (rs.next()) {
                        var customer = customers.get(rs.getString("cust_id"));
                        if (customer == null) {
                            customer = new Customer();
                            customer.setId(rs.getString("cust_id"));
                            customer.setName(rs.getString("cust_name"));
                            customers.put(customer.getId(), customer);
                        }
                        if (rs.getString("cont_id") != null) {
                            customer.addContact(
                                    new CustomerContact(
                                            rs.getString("cont_id"),
                                            CustomerContact.Type.valueOf(rs.getString("cont_type")),
                                            rs.getString("cont_contact"),
                                            customer
                                    )
                            );
                        }
                    }
                    return new ArrayList<>(customers.values());
                });
    }

    @Override
    public Customer save(Customer entity) {
        jdbc.update(
                "insert into customer (id, name) values (?, ?)",
                entity.getId(), entity.getName()
        );
        jdbc.batchUpdate("insert into customer_contact (id, type, contact, customer_id) values (?, ?, ?, ?)",
                entity.getContacts().stream()
                        .map(customerContact -> new Object[]{
                                customerContact.getId(),
                                customerContact.getType().name(),
                                customerContact.getContact(),
                                customerContact.getUser().getId(),
                        })
                        .collect(Collectors.toList()));
        return entity;
    }

    @Override
    public void update(Customer entity) {
        jdbc.update(
                "update customer set name=? where id=?",
                entity.getName(), entity.getId()
        );

        var savedCustomer = findById(entity.getId())
                .orElseThrow(() -> new DataRetrievalFailureException("Customer with id=" + entity.getId() + " not found"));

        jdbc.batchUpdate("insert into customer_contact (id, type, contact, customer_id) values (?, ?, ?, ?)", entity.getContacts()
                .stream()
                .filter(contact -> savedCustomer.getContacts().stream()
                        .noneMatch(savedContact -> savedContact.getId().equals(contact.getId())))
                .map(customerContact -> new Object[]{
                        customerContact.getId(),
                        customerContact.getType().name(),
                        customerContact.getContact(),
                        customerContact.getUser().getId(),
                })
                .collect(Collectors.toList()));
        jdbc.batchUpdate("update customer_contact set type=?, contact=? where id=?", entity.getContacts()
                .stream()
                .filter(contact -> savedCustomer.getContacts().stream()
                        .anyMatch(savedContact -> savedContact.getId().equals(contact.getId())))
                .map(contact -> new Object[]{contact.getType().name(), contact.getContact(), contact.getId()})
                .collect(Collectors.toList()));
        jdbc.batchUpdate("delete from customer_contact where id=?", savedCustomer.getContacts()
                .stream()
                .filter(savedContact -> entity.getContacts().stream()
                        .noneMatch(contact -> savedContact.getId().equals(contact.getId())))
                .map(savedContact -> new Object[]{savedContact.getId()})
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteById(String id) {
        jdbc.update("delete from customer where id=?", id);
    }
}
