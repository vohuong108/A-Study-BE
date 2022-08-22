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

import java.util.*;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    /** ==============================================================
     * DTO ===> ENTITY
     * ===============================================================
     * */
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
            @Mapping(target = "name", source = "quizDto.name"),
            @Mapping(target = "isActive", expression = "java(true)"),
            @Mapping(
                    target = "questions",
                    expression = "java(questionDtoListToQuestionSet(quizDto.getQuestions(), quiz))"
            )
    })
    Quiz quizDtoToQuiz(@NonNull QuizDto quizDto, @NonNull Week week);

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



    /** ==============================================================
     * ENTITY ===> DTO
     * ===============================================================
     * */

    @Mappings({
            @Mapping(target = "questions", ignore = true),
            @Mapping(target = "contentOrder", ignore = true),
            @Mapping(target = "contentStatus", ignore = true),
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true),
            @Mapping(target = "sessionId", ignore = true)
    })
    QuizDto quizToQuizDtoOverview(Quiz quiz, Long weekId);

    @Mappings({
            @Mapping(target = "weekId", ignore = true),
            @Mapping(target = "contentOrder", ignore = true),
            @Mapping(target = "contentStatus", ignore = true),
            @Mapping(target = "closeDate", ignore = true),
            @Mapping(target = "releaseDate", ignore = true),
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true),
            @Mapping(target = "sessionId", source = "sessionId"),
            @Mapping(target = "questions", source = "quiz.questions", qualifiedByName = "setQuestionToListQuestionForDo")
    })
    QuizDto quizToQuizDtoForDo(Quiz quiz, Long sessionId);
    @Named("optionToOptionDtoForDo")
    @Mapping(target = "isSelect", ignore = true)
    @Mapping(target = "isCorrect", ignore = true)
    OptionDto optionToOptionDtoForDo(Option optionForDo);

    @Named("setOptionToListOptionForDo")
    @IterableMapping(qualifiedByName = "optionToOptionDtoForDo")
    List<OptionDto> setOptionToListOptionForDo(Set<Option> optionSetForDo);

    @Named("questionToQuestionDtoForDo")
    @Mapping(target = "isCorrect", ignore = true)
    @Mapping(target = "options", source = "questionForDo.options", qualifiedByName = "setOptionToListOptionForDo")
    QuestionDto questionToQuestionDtoForDo(Question questionForDo);

    @Named("setQuestionToListQuestionForDo")
    @IterableMapping(qualifiedByName = "questionToQuestionDtoForDo")
    List<QuestionDto> setQuestionToListQuestionForDo(Set<Question> questionSetForDo);

    @Mappings({
            @Mapping(target = "contentPath", ignore = true),
            @Mapping(target = "url", ignore = true),
            @Mapping(target = "sessionId", ignore = true),
            @Mapping(target = "questions", source = "quiz.questions", qualifiedByName = "setQuestionToListQuestionForEdit")
    })
    QuizDto quizToQuizDtoForEdit(Quiz quiz, Long weekId);

    @Named("optionToOptionDtoForEdit")
    @Mapping(target = "isSelect", ignore = true)
    OptionDto optionToOptionDtoForEdit(Option optionForEdit);

    @Named("setOptionToListOptionForEdit")
    @IterableMapping(qualifiedByName = "optionToOptionDtoForEdit")
    List<OptionDto> setOptionToListOptionForEdit(Set<Option> optionSetForEdit);

    @Named("questionToQuestionDtoForEdit")
    @Mapping(target = "isCorrect", ignore = true)
    @Mapping(target = "options", source = "questionForEdit.options", qualifiedByName = "setOptionToListOptionForEdit")
    QuestionDto questionToQuestionDtoForEdit(Question questionForEdit);

    @Named("setQuestionToListQuestionForEdit")
    @IterableMapping(qualifiedByName = "questionToQuestionDtoForEdit")
    List<QuestionDto> setQuestionToListQuestionForEdit(Set<Question> questionSetForEdit);
}
