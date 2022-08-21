package com.example.astudy.entities.projections;

import com.example.astudy.enums.CourseContentStatus;
import com.example.astudy.enums.CourseContentType;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface WeekContentProjection extends Serializable {
     @Value("#{target.id}")
     Long getID();
     @Value("#{target.content_order}")
     Integer getContentOrder();

     @Value("#{target.name}")
     String getName();
     @Value("#{target.content_status}")
     CourseContentStatus getContentStatus();
     @Value("#{target.content_type}")
     CourseContentType getContentType();
}
