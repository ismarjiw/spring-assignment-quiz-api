package com.cooksys.quiz_api.repositories;

import com.cooksys.quiz_api.entities.Answer;

import com.cooksys.quiz_api.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// You may think you don't need this Repository, but remember each Repository interface
// only allows you to interact with the 1 table it maps to, so in order to save or retrieve
// answers directly you need to use this interface. You can still access Answers stored on a Question
// without using this interface.
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    void deleteByQuestionIn(List<Question> questions);


}
