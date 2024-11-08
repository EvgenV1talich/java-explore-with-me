package ru.yandex.service.adminapi.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.dto.category.NewCategoryDto;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.mapper.CategoryMapper;
import ru.yandex.model.category.Category;
import ru.yandex.repository.CategoryRepository;
import ru.yandex.repository.EventRepository;

import java.text.MessageFormat;

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
        try {
            category = repository.save(mapper.toCategoryFromNewCategoryDto(newCategoryDto));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Failed to save category in adminCategoryService");
        }

        log.info(MessageFormat
                .format("Added category - {0}", category.getName()));

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

        log.info(MessageFormat
                .format("Category (name = {0}) was deleted", category.getName()));
    }

    @Override
    @Transactional
    public Category updateCategoryById(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = repository.findById(categoryId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("Category with id={0} was not found", categoryId)));

        try {
            category.setName(newCategoryDto.getName());
            repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Failed to save category in adminCategoryService");
        }

        log.info(MessageFormat
                .format("Changed name {0} to {1}", category.getId(), category.getName()));

        return category;
    }

}
