package com.example.astudy.entities.projections;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface SearchCourseProjection extends Serializable {
    @Value("#{target.id}")
    Long getID();
    @Value("#{target.name}")
    String getName();
    @Value("#{target.author_name}")
    String getAuthor();
}
