package ru.dexsys.customers;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    private String id;
    private String name;
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
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
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
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
