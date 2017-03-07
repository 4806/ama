package org.sysc.ama.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.sysc.ama.model.Ama;

public interface AmaRepository extends CrudRepository<Ama, Long> {

    Optional<Ama> findById(Long id);

    List<Ama> findAllByIsPublic (boolean isPublic, Pageable request);

}
