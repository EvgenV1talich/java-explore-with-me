package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {


}
