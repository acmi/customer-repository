package ru.dexsys.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerContactRepository customerContactRepository;
    @Autowired
    private EntityManager entityManager;

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
        entityManager.flush();

        entityManager.clear();


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
        entityManager.flush();

        entityManager.clear();

        customerContactRepository.deleteById("customer_contact_mobile");
        entityManager.flush();

        var savedCustomer = customerRepository.findById("customer_id");
        assertTrue(savedCustomer.isPresent());
        assertTrue(savedCustomer.get().getContacts().isEmpty());
    }
}
