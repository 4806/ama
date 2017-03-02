package org.sysc.ama.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Date;

public interface AmaRepository extends CrudRepository<Ama, Long> {

    Ama findById(Long id);

    List<Ama> findAllByIsPublic (boolean isPublic, Pageable request);

}
