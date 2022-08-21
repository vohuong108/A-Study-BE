package com.example.astudy.services.serviceImpl;

import com.example.astudy.entities.WeekContent;
import com.example.astudy.repositories.WeekContentRepo;
import com.example.astudy.services.WeekContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class WeekContentServiceImpl implements WeekContentService {
    private final WeekContentRepo weekContentRepo;

    @Override
    public WeekContent save(WeekContent weekContent) {
        return weekContentRepo.save(weekContent);
    }
}
