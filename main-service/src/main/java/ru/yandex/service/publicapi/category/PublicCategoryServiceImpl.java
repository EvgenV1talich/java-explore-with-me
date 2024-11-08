package ru.yandex.service.publicapi.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.model.category.Category;
import ru.yandex.repository.CategoryRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository repository;

    @Override
    public List<Category> getCategories() {
        return repository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return repository.findById(categoryId).orElseThrow(() -> new NotFoundException(MessageFormat
                .format("Category (id = {0}) was not found", categoryId)));
    }
}
