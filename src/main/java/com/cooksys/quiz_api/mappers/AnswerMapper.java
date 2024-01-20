package com.cooksys.quiz_api.mappers;

import com.cooksys.quiz_api.dtos.AnswerResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

//  Answer requestDtoToEntity(AnswerRequestDto answerRequestDto);

  AnswerResponseDto entityToDto(Answer entity);

  List<AnswerResponseDto> entitiesToDtos(List<Answer> entities);

}
