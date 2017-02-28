package org.sysc.ama.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AmaRepository extends CrudRepository<Ama, Long> {

    List<Ama> findById(Long id);

}
