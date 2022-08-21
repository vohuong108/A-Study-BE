package com.example.astudy.dtos;

import com.example.astudy.enums.CourseContentStatus;
import com.example.astudy.enums.CourseContentType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LectureContentFormData {
    private String name;
    private int contentOrder;
    private CourseContentType contentType;
    private CourseContentStatus contentStatus;
    private MultipartFile file;
}
