package com.example.astudy.repositories;

import com.example.astudy.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepo extends JpaRepository<Lecture, Long> {
    @Query(
            value = "SELECT content_path  FROM lecture l WHERE l.id = :id",
            nativeQuery = true
    )
    String findContentPathByLectureId(@Param("id") Long id);
}
