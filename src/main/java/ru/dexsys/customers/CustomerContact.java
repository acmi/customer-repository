package ru.dexsys.customers;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CustomerContact {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String contact;
    @ManyToOne
    private Customer customer;

    public CustomerContact() {
    }

    public CustomerContact(String id, Type type, String contact, Customer customer) {
        this.id = id;
        this.type = type;
        this.contact = contact;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Customer getUser() {
        return customer;
    }

    public void setUser(Customer customer) {
        this.customer = customer;
    }

    public enum Type {
        MOBILE,
        EMAIL
    }
}
