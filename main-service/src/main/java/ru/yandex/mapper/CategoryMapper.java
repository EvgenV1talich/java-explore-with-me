package ru.yandex.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.dto.CategoryDto;
import ru.yandex.dto.NewCategoryDto;
import ru.yandex.model.category.Category;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category toCategoryFromNewCategoryDto(NewCategoryDto dto) {
        if (dto == null) {
            return null;
        }
        return new Category(
                null,
                dto.getName()
        );
    }

}
