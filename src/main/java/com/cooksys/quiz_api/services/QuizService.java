package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;

public interface QuizService {

  List<QuizResponseDto> getAllQuizzes();

  QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

  QuizResponseDto deleteQuizById(Long id);

  QuizResponseDto renameQuiz(Long id, String newName);
}
