package org.sysc.ama.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Date;

import org.sysc.ama.model.Question;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    Question findById(Long id);

    List<Question> findByAma(Ama ama, Pageable request);

}
