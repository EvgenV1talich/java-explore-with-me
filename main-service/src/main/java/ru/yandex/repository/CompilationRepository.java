package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
}
