package com.example.astudy.services;

import com.example.astudy.entities.Category;

import java.util.List;

public interface CommonService {
    List<Category> getCategories();
    Category saveCategory(Category category);

}
