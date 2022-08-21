package com.example.astudy.dtos.mapper;


import com.example.astudy.dtos.WeekDto;
import com.example.astudy.entities.Week;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WeekMapper {
    @Mappings({
            @Mapping(target = "ID", ignore = true),
            @Mapping(target = "course", ignore = true),
            @Mapping(target = "weekContents", ignore = true),
    })
    Week weekDtoToWeek(WeekDto weekDto);

    @Mappings({
            @Mapping(target = "courseId", ignore = true),
            @Mapping(target = "contents", ignore = true)
    })
    WeekDto weekToPlainWeekDto(Week week);
}
