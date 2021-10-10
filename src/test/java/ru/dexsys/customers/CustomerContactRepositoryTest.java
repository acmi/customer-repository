package ru.dexsys.customers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerContactRepositoryTest {

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
    public void testDeleteCustomerContact() {
        var customer = new Customer();
        customer.setId("customer_id");
        customer.setName("John Mathew");

        var mobile = new CustomerContact();
        mobile.setId("customer_contact_mobile");
        mobile.setContact("+79123456789");
        mobile.setType(CustomerContact.Type.MOBILE);
        customer.addContact(mobile);

        customerRepository.save(customer);

        customerContactRepository.deleteById("customer_contact_mobile");

        var savedCustomer = customerRepository.findById("customer_id");
        assertTrue(savedCustomer.isPresent());
        assertTrue(savedCustomer.get().getContacts().isEmpty());
    }

    @Test
    public void testSaveCustomerContact() {
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("Bob");

        CustomerContact contact = new CustomerContact();
        contact.setId("1");
        contact.setContact("+79999999999");
        contact.setType(CustomerContact.Type.MOBILE);
        customer.addContact(contact);

        customerContactRepository.save(contact);

        Optional<CustomerContact> savedContact = customerContactRepository.findById("1");

        assertTrue(savedContact.isPresent());
        assertEquals("+79999999999", savedContact.get().getContact());
        assertEquals(CustomerContact.Type.MOBILE, savedContact.get().getType());
        assertEquals("Bob", savedContact.get().getUser().getName());
    }

    @Test
    public void testFindAllCustomerContact() {
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("Tom");

        CustomerContact mobile = new CustomerContact();
        mobile.setId("1");
        mobile.setContact("+79999999999");
        mobile.setType(CustomerContact.Type.MOBILE);
        customer.addContact(mobile);

        Customer otherCustomer = new Customer();
        otherCustomer.setId("2");
        otherCustomer.setName("Bob");

        CustomerContact email = new CustomerContact();
        email.setId("2");
        email.setContact("123@example.org");
        email.setType(CustomerContact.Type.EMAIL);
        otherCustomer.addContact(email);

        customerContactRepository.save(mobile);
        customerContactRepository.save(email);

        List<CustomerContact> customerContacts = customerContactRepository.findAll();

        assertEquals(2, customerContacts.size());
        assertEquals("+79999999999", customerContacts.get(0).getContact());
        assertEquals(CustomerContact.Type.MOBILE, customerContacts.get(0).getType());
        assertEquals("123@example.org", customerContacts.get(1).getContact());
        assertEquals(CustomerContact.Type.EMAIL, customerContacts.get(1).getType());
        assertEquals("Tom", customerContacts.get(0).getUser().getName());
        assertEquals("Bob", customerContacts.get(1).getUser().getName());
    }

    @Test
    public void testUpdateCustomerContact() {
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

        Optional<CustomerContact> savedMobile = customerContactRepository.findById("1");
        Optional<CustomerContact> savedEmail = customerContactRepository.findById("2");
        CustomerContact updateMobile = null;
        CustomerContact updateEmail = null;

        if (savedMobile.isPresent() && savedEmail.isPresent()) {
            updateMobile = savedMobile.get();
            updateEmail = savedEmail.get();
        }

        updateMobile.setType(CustomerContact.Type.EMAIL);
        updateMobile.setContact("test@org.com");
        updateEmail.setType(CustomerContact.Type.MOBILE);
        updateEmail.setContact("+78888888888");

        customerContactRepository.update(updateMobile);
        customerContactRepository.update(updateEmail);

        Optional<CustomerContact> finalMobile = customerContactRepository.findById("1");
        Optional<CustomerContact> finalEmail = customerContactRepository.findById("2");

        assertTrue(finalMobile.isPresent());
        assertTrue(finalEmail.isPresent());
        assertEquals("test@org.com", finalMobile.get().getContact());
        assertEquals(CustomerContact.Type.EMAIL, finalMobile.get().getType());
        assertEquals("+78888888888", finalEmail.get().getContact());
        assertEquals(CustomerContact.Type.MOBILE, finalEmail.get().getType());
    }
}
