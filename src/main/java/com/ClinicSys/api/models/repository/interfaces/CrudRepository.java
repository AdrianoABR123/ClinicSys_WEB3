package com.ClinicSys.api.models.repository.interfaces;

public interface CrudRepository<T> {

    boolean createEntity(T entidade);
    T readEntityId(Long id);
    boolean updateEntity(T entidadeModificada);
    boolean deleteEntity(Long id);
}
