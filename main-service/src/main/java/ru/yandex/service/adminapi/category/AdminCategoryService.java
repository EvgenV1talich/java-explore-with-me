package ru.yandex.service.adminapi.category;

import ru.yandex.dto.category.NewCategoryDto;
import ru.yandex.model.category.Category;


public interface AdminCategoryService {
    Category addCategory(NewCategoryDto newCategoryDto);

    void deleteCategoryById(long categoryId);

    Category updateCategoryById(long categoryId, NewCategoryDto newCategoryDto);
}
