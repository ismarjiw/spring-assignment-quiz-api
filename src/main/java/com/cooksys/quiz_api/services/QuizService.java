package com.cooksys.quiz_api.services;

import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Question;

import java.util.List;

public interface QuizService {

    List<QuizResponseDto> getAllQuizzes();

    QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

    QuizResponseDto deleteQuizById(Long id);

    QuizResponseDto renameQuiz(Long id, String newName);

    QuestionResponseDto randomQuestion(Long id);

    QuizResponseDto addQuestion(Long id, Question question);

    QuestionResponseDto deleteQuestionFromQuiz(Long id, Long questionID);
}
