package org.sysc.ama.repo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.sysc.ama.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findById(Long id);
    User findByName(String name);

}
