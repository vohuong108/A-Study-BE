package com.example.astudy.dtos.mapper;

import com.example.astudy.dtos.OptionDto;
import com.example.astudy.dtos.QuestionDto;
import com.example.astudy.dtos.QuizDto;
import com.example.astudy.entities.Option;
import com.example.astudy.entities.Question;
import com.example.astudy.entities.Quiz;
import com.example.astudy.entities.Week;
import lombok.NonNull;
import org.mapstruct.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    @Mappings({
            @Mapping(target = "ID", ignore = true),
            @Mapping(target = "name", source = "quizDto.name"),
            @Mapping(target = "isActive", expression = "java(true)"),
            @Mapping(
                    target = "questions",
                    expression = "java(questionDtoListToQuestionSet(quizDto.getQuestions(), quiz))"
            )
    })
    Quiz quizDtoToQuiz(@NonNull QuizDto quizDto, @NonNull Week week);

    default Set<Question> questionDtoListToQuestionSet(List<QuestionDto> list, Quiz quiz) {
        if ( list == null ) {
            return null;
        }

        Set<Question> set = new LinkedHashSet<Question>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( QuestionDto questionDto : list ) {
            set.add( questionDtoToQuestion( questionDto, quiz ) );
        }

        return set;
    }

    default Set<Option> optionDtoListToOptionSet(List<OptionDto> list, Question question) {
        if ( list == null ) {
            return null;
        }

        Set<Option> set = new LinkedHashSet<Option>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( OptionDto optionDto : list ) {
            set.add( optionDtoToOption( optionDto, question ) );
        }

        return set;
    }

    @Mappings({
            @Mapping(target = "ID", ignore = true),
            @Mapping(target = "name", source = "questionDto.name"),
            @Mapping(
                    target = "options",
                    expression = "java(optionDtoListToOptionSet(questionDto.getOptions(), question))"
            )
    })
    Question questionDtoToQuestion(@NonNull QuestionDto questionDto, Quiz quiz);

    @Mappings({
            @Mapping(target = "ID", ignore = true)
    })
    Option optionDtoToOption(@NonNull OptionDto optionDto, Question question);

    @Mappings({
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    QuizDto quizToQuizDto(Quiz quiz, Long weekId);

    @Mappings({
            @Mapping(target = "questions", ignore = true),
            @Mapping(target = "contentOrder", ignore = true),
            @Mapping(target = "contentStatus", ignore = true),
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    QuizDto quizToQuizDtoOverview(Quiz quiz, Long weekId);


    @Mappings({
            @Mapping(target = "isCorrect", ignore = true)

    })
    OptionDto optionToOptionDtoForDo(Option option);

    @Mappings({
            @Mapping(target = "weekId", ignore = true),
            @Mapping(target = "contentOrder", ignore = true),
            @Mapping(target = "contentStatus", ignore = true),
            @Mapping(target = "closeDate", ignore = true),
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true),
            @Mapping(target = "sessionId", source = "sessionId")
    })
    QuizDto quizToQuizDtoForDo(Quiz quiz, Long sessionId);
}
