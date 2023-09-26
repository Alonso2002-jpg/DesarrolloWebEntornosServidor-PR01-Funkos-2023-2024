package org.develop.repository;

import java.util.Optional;

public interface FunkoRepository<T,ID> extends CRUDrepository<T,ID>{
    Optional<T> findByName(String name);

}
