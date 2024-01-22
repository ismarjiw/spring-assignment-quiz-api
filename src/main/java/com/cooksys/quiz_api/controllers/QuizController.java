package com.cooksys.quiz_api.controllers;

import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public QuizResponseDto deleteQuiz(
            @PathVariable Long id
    ) {
        return quizService.deleteQuizById(id);
    }

    @PatchMapping("/{id}/rename/{newName}")
    public QuizResponseDto renameQuiz(
            @PathVariable Long id,
            @PathVariable String newName
    ) {
        return quizService.renameQuiz(id, newName);
    }

    @GetMapping("/{id}/random")
    public QuestionResponseDto randomQuestion(
            @PathVariable Long id
    ) {
        return quizService.randomQuestion(id);
    }

    @PatchMapping("/{id}/add")
    public QuizResponseDto addQuestion(
            @PathVariable Long id,
            @RequestBody Question question
    ) {
        return quizService.addQuestion(id, question);
    }

    @DeleteMapping("/{id}/delete/{questionID}")
    public QuestionResponseDto deleteQuestionFromQuiz(
            @PathVariable Long id,
            @PathVariable Long questionID
    ) {

        return quizService.deleteQuestionFromQuiz(id, questionID);
    }
}
