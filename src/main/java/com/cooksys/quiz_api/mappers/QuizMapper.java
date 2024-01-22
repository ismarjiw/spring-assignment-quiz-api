package com.cooksys.quiz_api.mappers;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuizMapper {

    Quiz requestDtoToEntity(QuizRequestDto quizRequestDto);

    QuizResponseDto entityToDto(Quiz entity);

    List<QuizResponseDto> entitiesToDtos(List<Quiz> entities);
}
