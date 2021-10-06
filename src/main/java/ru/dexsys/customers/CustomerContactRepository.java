package ru.dexsys.customers;

import java.util.List;
import java.util.Optional;

public interface CustomerContactRepository {
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
    void update(CustomerContact entity);
    /**
     * удалить запить по Id
     */
    void deleteById(String id);
}
