package org.sysc.ama.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {

    Optional<Answer> findById(Long id);
}
