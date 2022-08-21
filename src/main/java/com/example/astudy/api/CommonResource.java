package com.example.astudy.api;

import com.example.astudy.dtos.ResponseBodyInfo;
import com.example.astudy.entities.Category;
import com.example.astudy.services.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import static com.example.astudy.constant.SecurityConstants.API_V1_COMMON_ROOT_URL;


@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_COMMON_ROOT_URL)
@Slf4j
public class CommonResource {

    private final CommonService commonService;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(HttpServletRequest request){
        log.info("Header origin in request {}", request.getHeader("Origin"));
        String uri = request.getRequestURI();

        return ResponseEntity
                .ok()
                .body(new ResponseBodyInfo(uri, commonService.getCategories()));
    }

    @PostMapping("/category/save")
    public ResponseEntity<?> saveCategory(HttpServletRequest request,
                                                 @RequestBody Category category) {

        String uri = request.getRequestURI();
        return ResponseEntity
                .ok()
                .body(new ResponseBodyInfo(uri, commonService.saveCategory(category)));
    }
}
