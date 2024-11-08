package ru.yandex.service.adminapi.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.dto.category.NewCategoryDto;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.IncorrectParameterException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.mapper.CategoryMapper;
import ru.yandex.model.category.Category;
import ru.yandex.repository.CategoryRepository;
import ru.yandex.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public Category addCategory(NewCategoryDto newCategoryDto) {
        Category category;
        if (newCategoryDto.getName().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is too long!");
        }

        try {
            category = repository.save(mapper.toCategoryFromNewCategoryDto(newCategoryDto));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Failed to save category in adminCategoryService");
        }

        log.info("Added category - " + category.getName());

        return category;
    }

    @Override
    @Transactional
    public void deleteCategoryById(long categoryId) {
        Category category = repository.findById(categoryId).orElseThrow();

        if (eventRepository.existsByCategory(category)) {
            throw new ConflictException("Category already exists");
        } else {
            repository.deleteById(categoryId);
        }

        log.info("Category (name = " + category.getName() + ") was deleted");
    }

    @Override
    @Transactional
    public Category updateCategoryById(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = repository.findById(categoryId).orElseThrow(()
                -> new NotFoundException("Category with id=" + categoryId + " was not found"));

        if (newCategoryDto.getName().length() > 50) {
            throw new IncorrectParameterException("Field too long (more than 50 symbols)");
        }

        try {
            category.setName(newCategoryDto.getName());
            repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Failed to save category in adminCategoryService");
        }

        log.info("Changed name " + category.getId() + " to " + category.getName());

        return category;
    }

}
