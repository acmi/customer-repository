package ru.dexsys.customers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private CustomerContactRepository customerContactRepository;

    @BeforeEach
    public void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setName("users")
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("schema.sql")
                .build();

        customerRepository = new SpringCustomerRepository(dataSource);
        customerContactRepository = new SpringCustomerContactRepository(dataSource);
    }

    @Test
    public void testCustomerSaved() {
        var customer = new Customer();
        customer.setId("customer_id");
        customer.setName("John Mathew");

        var home = new CustomerContact();
        home.setId("customer_contact_email");
        home.setContact("John.Mathew@xyz.com");
        home.setType(CustomerContact.Type.EMAIL);
        customer.addContact(home);

        var mobile = new CustomerContact();
        mobile.setId("customer_contact_mobile");
        mobile.setContact("+79123456789");
        mobile.setType(CustomerContact.Type.MOBILE);
        customer.addContact(mobile);

        customerRepository.save(customer);


        var savedCustomer = customerRepository.findById("customer_id");
        assertTrue(savedCustomer.isPresent());
        assertEquals("John Mathew", savedCustomer.get().getName());
        assertEquals(2, savedCustomer.get().getContacts().size());
        assertEquals("John.Mathew@xyz.com", savedCustomer.get().getContacts().get(0).getContact());
        assertEquals(CustomerContact.Type.EMAIL, savedCustomer.get().getContacts().get(0).getType());
        assertEquals("+79123456789", savedCustomer.get().getContacts().get(1).getContact());
        assertEquals(CustomerContact.Type.MOBILE, savedCustomer.get().getContacts().get(1).getType());
    }

    @Test
    public void testCustomerFindAll() {
        Customer firstCustomer = new Customer();
        firstCustomer.setId("1");
        firstCustomer.setName("Tom");

        CustomerContact firstCustomerContactMobile = new CustomerContact();
        firstCustomerContactMobile.setId("1");
        firstCustomerContactMobile.setContact("+79999999999");
        firstCustomerContactMobile.setType(CustomerContact.Type.MOBILE);
        firstCustomer.addContact(firstCustomerContactMobile);

        CustomerContact firstCustomerContactEmail = new CustomerContact();
        firstCustomerContactEmail.setId("2");
        firstCustomerContactEmail.setContact("123@example.org");
        firstCustomerContactEmail.setType(CustomerContact.Type.EMAIL);
        firstCustomer.addContact(firstCustomerContactEmail);

        Customer secondCustomer = new Customer();
        secondCustomer.setId("2");
        secondCustomer.setName("Bob");

        CustomerContact secondCustomerContactMobile = new CustomerContact();
        secondCustomerContactMobile.setId("3");
        secondCustomerContactMobile.setContact("+78888888888");
        secondCustomerContactMobile.setType(CustomerContact.Type.MOBILE);
        secondCustomer.addContact(secondCustomerContactMobile);

        CustomerContact secondCustomerContactEmail = new CustomerContact();
        secondCustomerContactEmail.setId("4");
        secondCustomerContactEmail.setContact("test@org.com");
        secondCustomerContactEmail.setType(CustomerContact.Type.EMAIL);
        secondCustomer.addContact(secondCustomerContactEmail);

        customerRepository.save(firstCustomer);
        customerRepository.save(secondCustomer);

        List<Customer> customers = customerRepository.findAll();

        assertEquals(2, customers.size());
        assertEquals("Tom", customers.get(0).getName());
        assertEquals("Bob", customers.get(1).getName());
        assertEquals("+79999999999", customers.get(0).getContacts().get(0).getContact());
        assertEquals("test@org.com", customers.get(1).getContacts().get(1).getContact());
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("Tom");

        CustomerContact mobile = new CustomerContact();
        mobile.setId("1");
        mobile.setContact("+79999999999");
        mobile.setType(CustomerContact.Type.MOBILE);
        customer.addContact(mobile);

        CustomerContact email = new CustomerContact();
        email.setId("2");
        email.setContact("123@example.org");
        email.setType(CustomerContact.Type.EMAIL);
        customer.addContact(email);

        customerRepository.save(customer);

        Optional<Customer> savedCustomerOptional = customerRepository.findById("1");
        Customer savedCustomer = null;
        if (savedCustomerOptional.isPresent()) {
            savedCustomer = savedCustomerOptional.get();
        }

        savedCustomer.setName("Bob");
        savedCustomer.getContacts().get(0).setContact("+78888888888");
        savedCustomer.getContacts().get(1).setContact("test@org.com");
        customerRepository.update(savedCustomer);

        Optional<Customer> finalCustomer = customerRepository.findById("1");

        assertTrue(finalCustomer.isPresent());
        assertEquals("Bob", finalCustomer.get().getName());
        assertEquals("+78888888888", finalCustomer.get().getContacts().get(0).getContact());
        assertEquals("test@org.com", finalCustomer.get().getContacts().get(1).getContact());
    }

    @Test
    public void testDeleteByIdCustomer() {
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("Tom");

        CustomerContact contact = new CustomerContact();
        contact.setId("1");
        contact.setContact("+79999999999");
        contact.setType(CustomerContact.Type.MOBILE);
        customer.addContact(contact);

        customerRepository.save(customer);

        customerRepository.deleteById("1");

        Optional<Customer> deleteCustomer = customerRepository.findById("1");
        Optional<CustomerContact> deleteCustomerContact = customerContactRepository.findById("1");

        assertFalse(deleteCustomer.isPresent());
        assertFalse(deleteCustomerContact.isPresent());
    }
}
