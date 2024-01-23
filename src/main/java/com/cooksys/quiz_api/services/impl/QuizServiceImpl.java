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

    String QUIZ_NOT_FOUND_MESSAGE = "Quiz not found with ID: ";
    String QUESTION_NOT_FOUND_MESSAGE = "Question not found with ID: ";

    @Override
    public List<QuizResponseDto> getAllQuizzes() {
        return quizMapper.entitiesToDtos(quizRepository.findAll());
    }

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Error creating quiz");
        }
    }

    @Override
    public QuizResponseDto deleteQuizById(Long id) {
        Optional<Quiz> quizToDelete = quizRepository.findById(id);

        if (quizToDelete.isPresent()) {
            Quiz deletedQuiz = quizToDelete.get();

            try {
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
            } catch (Exception e) {
                throw new RuntimeException("Error deleting quiz");
            }
        } else {
            throw new NoSuchElementException(QUIZ_NOT_FOUND_MESSAGE + id);
        }
    }

    @Override
    public QuizResponseDto renameQuiz(Long id, String newName) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);

            if (optionalQuiz.isPresent()) {
                Quiz renamedQuiz = optionalQuiz.get();
                renamedQuiz.setName(newName);
                return quizMapper.entityToDto(renamedQuiz);
            } else {
                throw new NoSuchElementException(QUIZ_NOT_FOUND_MESSAGE + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error renaming quiz");
        }
    }

    @Override
    public QuestionResponseDto randomQuestion(Long id) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);

            if (optionalQuiz.isPresent()) {
                Quiz selectedQuiz = optionalQuiz.get();
                List<Question> questions = selectedQuiz.getQuestions();

                if (!questions.isEmpty()) {
                    int randomIndex = (int) (Math.random() * questions.size());
                    Question randomQuestion = questions.get(randomIndex);
                    return questionMapper.entityToDto(randomQuestion);
                } else {
                    throw new IllegalStateException("Quiz with ID " + id + " has no questions");
                }
            } else {
                throw new NoSuchElementException(QUIZ_NOT_FOUND_MESSAGE + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving random question");
        }
    }

    @Override
    public QuizResponseDto addQuestion(Long id, Question question) {
        try {
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
                throw new NoSuchElementException(QUIZ_NOT_FOUND_MESSAGE + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding question to quiz");
        }
    }

    @Override
    public QuestionResponseDto deleteQuestionFromQuiz(Long id, Long questionID) {
        try {
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
                throw new NoSuchElementException(QUIZ_NOT_FOUND_MESSAGE + id + " or " + QUESTION_NOT_FOUND_MESSAGE + questionID);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting question from quiz");
        }
    }

}
