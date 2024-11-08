package ru.yandex.service.publicapi.category;

import ru.yandex.model.category.Category;

import java.util.List;

public interface PublicCategoryService {

    List<Category> getCategories();

    Category getCategoryById(Long categoryId);

}
