package org.sysc.ama.repo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import org.sysc.ama.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByName(String name);
}
