package org.sysc.ama.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.Answer;

import java.util.List;
import java.util.Optional;

/**
 * Created by cameronblanchard on 3/16/2017.
 */
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    Optional<Answer> findById(Long id);

    Optional<Answer> findByAma(Question question, Pageable request);
}
