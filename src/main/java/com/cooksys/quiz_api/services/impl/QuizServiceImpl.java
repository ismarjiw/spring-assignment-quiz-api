package com.cooksys.quiz_api.services.impl;

import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizMapper quizMapper;
    private final QuizRepository quizRepository;

    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    @Override
    public List<QuizResponseDto> getAllQuizzes() {

        return quizMapper.entitiesToDtos(quizRepository.findAll());
    }

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        Quiz quizToSave = quizMapper.requestDtoToEntity(quizRequestDto);
        Quiz savedQuiz = quizRepository.saveAndFlush(quizToSave);

        for (Question q : savedQuiz.getQuestions()) {
            q.setQuiz(savedQuiz);
            questionRepository.saveAndFlush(q);

            for (Answer a : q.getAnswers()) {
                a.setQuestion(q);
                answerRepository.saveAndFlush(a);
            }
        }

        return quizMapper.entityToDto(savedQuiz);
    }

    @Override
    public QuizResponseDto deleteQuizById(Long id) {
        Optional<Quiz> quizToDelete = quizRepository.findById(id);

        if (quizToDelete.isPresent()) {
            Quiz deletedQuiz = quizToDelete.get();

            for (Question q : deletedQuiz.getQuestions()) {
                for (Answer a : q.getAnswers()) {
                    answerRepository.deleteById(a.getId());
                }
            }

            for (Question q : deletedQuiz.getQuestions()) {
                questionRepository.deleteById(q.getId());
            }

            quizRepository.deleteById(id);

            return quizMapper.entityToDto(deletedQuiz);
        } else {
            throw new NoSuchElementException("Quiz not found with ID: " + id);
        }
    }

    @Override
    public QuizResponseDto renameQuiz(Long id, String newName) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);

        if (optionalQuiz.isPresent()) {
            Quiz renamedQuiz = optionalQuiz.get();
            renamedQuiz.setName(newName);
            return quizMapper.entityToDto(renamedQuiz);
        } else {
            throw new NoSuchElementException("Quiz not found with ID: " + id);
        }

    }

    @Override
    public QuestionResponseDto randomQuestion(Long id) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);

        if (optionalQuiz.isPresent()) {
            Quiz selectedQuiz = optionalQuiz.get();
            List<Question> questions = selectedQuiz.getQuestions();

            if (!questions.isEmpty()) {
                int randomIndex = (int) (Math.random() * questions.size());
                Question randomQuestion = questions.get(randomIndex);
                return questionMapper.entityToDto(randomQuestion);
            }
        } else {
            throw new NoSuchElementException("Quiz not found with ID: " + id);
        }
        return null;
    }

    @Override
    public QuizResponseDto addQuestion(Long id, Question question) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);

        if (optionalQuiz.isPresent()) {
            Quiz selectedQuiz = optionalQuiz.get();

            question.setQuiz(selectedQuiz);
            questionRepository.saveAndFlush(question);

            for (Answer a : question.getAnswers()) {
                a.setQuestion(question);
                answerRepository.saveAndFlush(a);
            }

            quizRepository.saveAndFlush(selectedQuiz);

            return quizMapper.entityToDto(selectedQuiz);
        } else {
            throw new NoSuchElementException("Quiz not found with ID: " + id);
        }
    }

    @Override
    public QuestionResponseDto deleteQuestionFromQuiz(Long id, Long questionID) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        Optional<Question> optionalQuestion = questionRepository.findById(questionID);

        if (optionalQuiz.isPresent() && optionalQuestion.isPresent()) {
            Quiz selectedQuiz = optionalQuiz.get();
            Question selectedQuestion = optionalQuestion.get();

            for (Answer a : selectedQuestion.getAnswers()) {
                answerRepository.deleteById(a.getId());
            }
            questionRepository.deleteById(questionID);

            quizRepository.saveAndFlush(selectedQuiz);

            return questionMapper.entityToDto(selectedQuestion);
        } else {
            throw new NoSuchElementException("Quiz not found with ID: " + id + "or Question not found with ID: " + questionID);
        }
    }

}
