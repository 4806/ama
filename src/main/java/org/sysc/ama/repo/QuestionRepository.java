package org.sysc.ama.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.sysc.ama.model.Question;
import org.sysc.ama.model.Ama;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    Optional<Question> findById(Long id);

    List<Question> findByAma(Ama ama, Pageable request);
    List<Question> findByAma(Ama ama);

    int countByAma(Ama ama);

}
