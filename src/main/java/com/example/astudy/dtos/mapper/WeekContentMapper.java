package com.example.astudy.dtos.mapper;

import com.example.astudy.dtos.QuizDto;
import com.example.astudy.dtos.WeekContentDto;
import com.example.astudy.entities.Lecture;
import com.example.astudy.entities.Quiz;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WeekContentMapper {

    @Mappings({
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true)
    })
    WeekContentDto lectureToWeekContentDto(Lecture lecture, Long weekId);
    @Mappings({
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true),
            @Mapping(target = "name", source = "quiz.name"),
            @Mapping(target = "weekId", expression = "java(quiz.getWeek().getID())")
    })
    WeekContentDto createdQuizToWeekContentDto(@NonNull Quiz quiz);
}
