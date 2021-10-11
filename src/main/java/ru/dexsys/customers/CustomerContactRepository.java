package ru.dexsys.customers;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CustomerContactRepository extends Repository<CustomerContact, String> {
    /**
     * вернуть объект по Id
     */
    Optional<CustomerContact> findById(String id);
    /**
     * вернуть все объекты
     */
    List<CustomerContact> findAll();
    /**
     * сохранить новый объект
     */
    CustomerContact save(CustomerContact entity);
    /**
     * обновить все поля по Id
     */
    default void update(CustomerContact entity) {
        save(entity);
    }
    /**
     * удалить запить по Id
     */
    void deleteById(String id);
}
