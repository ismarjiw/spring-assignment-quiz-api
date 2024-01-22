package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public List<QuizResponseDto> getAllQuizzes() {

    return quizService.getAllQuizzes();
  }
  @PostMapping
  public QuizResponseDto createQuiz(
          @RequestBody QuizRequestDto quizRequestDto
  ) {
    return quizService.createQuiz(quizRequestDto);
  }

//  @DeleteMapping("/{id}")
//  public QuizResponseDto deleteQuiz(
//          @PathVariable Long id
//  ) {
//    return quizService.deleteQuizById(id);
//  }

}