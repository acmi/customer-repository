package ru.dexsys.customers;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    /**
     * вернуть объект по Id
     */
    Optional<Customer> findById(String id);
    /**
     * вернуть все объекты
     */
    List<Customer> findAll();
    /**
     * сохранить новый объект
     */
    Customer save(Customer entity);
    /**
     * обновить все поля по Id
     */
    void update(Customer entity);
    /**
     * удалить запить по Id
     */
    void deleteById(String id);
}
