package com.example.astudy.repositories;

import com.example.astudy.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepo extends JpaRepository<Option, Long> {

    @Query(
            value = "SELECT id, content, is_correct, option_order, question_id " +
                    "FROM option o WHERE o.question_id IN :listIds",
            nativeQuery = true
    )
    List<Option> findOptionByQuestionIds(@Param("listIds") List<Long> listIds);
}
