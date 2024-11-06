package ru.yandex.service.adminapi.category;

import ru.yandex.dto.NewCategoryDto;
import ru.yandex.model.category.Category;

public interface AdminCategoryService {
    Category addCategory(NewCategoryDto newCategoryDto);

    void deleteCategoryById(long catId);

    Category updateCategoryById(long catId, NewCategoryDto newCategoryDto);
}
