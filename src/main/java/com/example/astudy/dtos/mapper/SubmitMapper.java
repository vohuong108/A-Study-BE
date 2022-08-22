package com.example.astudy.dtos.mapper;

import com.example.astudy.dtos.OptionDto;
import com.example.astudy.dtos.QuestionDto;
import com.example.astudy.dtos.SubmitDto;
import com.example.astudy.entities.Option;
import com.example.astudy.entities.Question;
import com.example.astudy.entities.Submit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmitMapper {

    @Mapping(target = "questions", ignore = true)
    SubmitDto submitToSubmitDto(Submit submit);

    List<SubmitDto> listSubmitToListSubmitDto(List<Submit> list);

    @Mapping(target = "options", ignore = true)
    @Mapping(target = "isCorrect", expression = "java(false)")
    QuestionDto questionToQuestionDto(Question question);

    @Mapping(target = "isCorrect", ignore = true)
    @Mapping(target = "isSelect", expression = "java(false)")
    OptionDto optionToOptionDto(Option option);
}
