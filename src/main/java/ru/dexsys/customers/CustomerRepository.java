package ru.dexsys.customers;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends Repository<Customer, String> {
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
    default void update(Customer entity) {
        save(entity);
    }
    /**
     * удалить запить по Id
     */
    void deleteById(String id);
}
