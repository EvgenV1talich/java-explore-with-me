package ru.yandex.controller.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.category.CategoryDto;
import ru.yandex.mapper.CategoryMapper;
import ru.yandex.model.category.Category;
import ru.yandex.service.publicapi.category.PublicCategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
public class PublicCategoryController {

    public final PublicCategoryService service;
    private final CategoryMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Recieved /GET request to PublicCategoryController...");
        return new ResponseEntity<>(pagedResponse(service.getCategories(), from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(value = "catId") Long categoryId) {
        log.info("Recieved /GET request to PublicCategoryController...");
        return new ResponseEntity<>(mapper.toCategoryDto(service.getCategoryById(categoryId)), HttpStatus.OK);
    }

    private List<CategoryDto> pagedResponse(List<Category> categories, int from, int size) {
        List<CategoryDto> pagedCategories = new ArrayList<>();

        int totalCompilations = categories.size();
        int toIndex = from + size;

        if (from <= totalCompilations) {
            if (toIndex > totalCompilations) {
                toIndex = totalCompilations;
            }
            for (Category category : categories.subList(from, toIndex)) {
                pagedCategories.add(mapper.toCategoryDto(category));
            }
            return pagedCategories;
        } else {
            return Collections.emptyList();
        }
    }
}
