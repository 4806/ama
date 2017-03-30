package org.sysc.ama.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.User;

public interface AmaRepository extends CrudRepository<Ama, Long> {

    Optional<Ama> findById(Long id);

    Optional<Ama> findByTitle(String name);

    List<Ama> findByAllowedUsersOrIsPublic(User user, boolean isPublic, Pageable request);
}
