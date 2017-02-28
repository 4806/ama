package org.sysc.ama.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Cameron on 27/02/2017.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findById(Long id);

}
