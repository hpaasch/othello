package com.compozed.repository;

import com.compozed.model.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by localadmin on 4/4/17.
 */
public interface GameRepository extends CrudRepository<Game, Long> {

}
