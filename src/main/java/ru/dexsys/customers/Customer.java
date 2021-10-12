package ru.dexsys.customers;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String id;
    private String name;
    private List<CustomerContact> contacts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomerContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<CustomerContact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(CustomerContact contact) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        contact.setUser(this);
        contacts.add(contact);
    }
}