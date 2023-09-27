package org.develop.repository;

import org.develop.model.Funko;

import java.util.List;

public interface FunkoRepository<T,ID> extends CRUDrepository<T,ID>{
    List<Funko> findByName(String name);

}
