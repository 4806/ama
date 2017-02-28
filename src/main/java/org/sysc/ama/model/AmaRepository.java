package org.sysc.ama.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Cameron on 27/02/2017.
 */
public interface AmaRepository extends CrudRepository<Ama, Long> {

    List<Ama> findById(Long id);

}
