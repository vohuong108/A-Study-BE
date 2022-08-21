package com.example.astudy.dtos.mapper;

import com.example.astudy.dtos.CourseDto;
import com.example.astudy.entities.Course;
import com.example.astudy.entities.CourseInfo;
import com.example.astudy.entities.StudentCourse;
import com.example.astudy.enums.CourseInfoType;
import lombok.NonNull;
import org.mapstruct.*;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mappings({
            @Mapping(target = "ID", source = "course.ID"),
            @Mapping(target = "name", source = "course.name"),
            @Mapping(target = "description", source = "course.description"),
            @Mapping(target = "releaseDate", source = "course.releaseDate"),
            @Mapping(target = "category", source = "course.category.name"),
            @Mapping(target = "enrolledAt", source = "enrolledAt"),
            @Mapping(target = "progress", source = "progress"),
            @Mapping(target = "skills", ignore = true),
            @Mapping(target = "learns", ignore = true),
            @Mapping(target = "author", ignore = true),
    })
    CourseDto studentCourseToCourseDto(StudentCourse studentCourse);

    @Mappings({
            @Mapping(target = "enrolledAt", ignore = true),
            @Mapping(target = "progress", ignore = true),
            @Mapping(target = "category", expression = "java(course.getCategory().getName())"),
            @Mapping(target = "author", expression = "java(course.getAuthor().getUsername())"),
            @Mapping(target = "skills", ignore = true),
            @Mapping(target = "learns", ignore = true)
    })
    CourseDto courseToCourseDto(Course course);

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "courseInfos", ignore = true),
            @Mapping(target = "releaseDate", expression = "java(new Date())")
    })
    Course courseDtoToCourse(CourseDto courseDto);

    @AfterMapping
    default void combineToCourseInfos(@NonNull CourseDto courseDto, @NonNull @MappingTarget Course course) {
        List<String> skills = courseDto.getSkills();
        List<String> learns = courseDto.getLearns();
        Set<CourseInfo> courseInfos = new HashSet<>();

        if (skills != null ) {
            courseInfos.addAll(skills.stream().map(x -> new CourseInfo(
                    null, x, CourseInfoType.SKILL, course
            )).collect(Collectors.toSet()));
        }

        if (learns != null) {
            courseInfos.addAll(skills.stream().map(x -> new CourseInfo(
                    null, x, CourseInfoType.LEARN, course
            )).collect(Collectors.toSet()));
        }

        course.setCourseInfos(courseInfos);
    }

//    @AfterMapping
//    default void splitToSkillAndLearn(Course course, @MappingTarget CourseDto courseDto) {
//        List<String> skills = new ArrayList<>();
//        List<String> learns = new ArrayList<>();
//
//        if (course.getCourseInfos() == null) {
//            return;
//        }
//        course.getCourseInfos().forEach(x -> {
//            if (x.getInfoType().equals(CourseInfoType.SKILL)) {
//                skills.add(x.getContent());
//            } else {
//                learns.add(x.getContent());
//            }
//        });
//
//        courseDto.setSkills(skills);
//        courseDto.setLearns(learns);
//    }

}
