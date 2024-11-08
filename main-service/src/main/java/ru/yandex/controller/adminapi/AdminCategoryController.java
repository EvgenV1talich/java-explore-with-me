package ru.yandex.controller.adminapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.category.CategoryDto;
import ru.yandex.dto.category.NewCategoryDto;
import ru.yandex.mapper.CategoryMapper;
import ru.yandex.service.adminapi.category.AdminCategoryService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService service;
    private final CategoryMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Recieved /POST request to AdminCategoryController...");
        return new ResponseEntity<>(mapper.toCategoryDto(service.addCategory(newCategoryDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable(value = "catId") int categoryId) {

        log.info("Recieved /DELETE request to AdminCategoryController...");
        service.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategoryById(@PathVariable(value = "catId") int categoryId,
                                                          @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Recieved /PATCH request to AdminCategoryController...");
        return new ResponseEntity<>(mapper.toCategoryDto(service.updateCategoryById(categoryId, newCategoryDto)), HttpStatus.OK);
    }

}
