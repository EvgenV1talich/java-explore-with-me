package ru.yandex.service.publicapi.compilation;

import ru.yandex.model.compilation.Compilation;

import java.util.List;

public interface PublicCompilationService {

    List<Compilation> getCompilations(Boolean pinned);

    Compilation getCompilationById(Integer compId);

}
