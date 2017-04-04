package com.compozed.repository;

import com.compozed.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by localadmin on 4/4/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
