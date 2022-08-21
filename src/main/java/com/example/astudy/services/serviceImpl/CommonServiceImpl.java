package com.example.astudy.services.serviceImpl;

import com.example.astudy.entities.Category;
import com.example.astudy.repositories.CategoryRepo;
import com.example.astudy.services.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    private final CategoryRepo categoryRepo;

    @Override
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }
}
