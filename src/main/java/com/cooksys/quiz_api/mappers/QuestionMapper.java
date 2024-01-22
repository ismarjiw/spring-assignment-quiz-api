package com.cooksys.quiz_api.mappers;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.entities.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { AnswerMapper.class })
public interface QuestionMapper {

  Question requestDtoToEntity(QuestionRequestDto questionRequestDto);

  QuestionResponseDto entityToDto(Question entity);

  List<QuestionResponseDto> entitiesToDtos(List<Question> entities);

}
