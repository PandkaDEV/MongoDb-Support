package org.pieszku.mongodb.repository;

import org.pieszku.mongodb.type.SaveType;

import java.util.Collection;

public interface CrudRepository<ID, T extends Identifiable<ID>> extends Repository<ID, T> {


    void update(T object, ID field, SaveType saveType);
    T find(ID id);
    Collection<T> findAll();
}
