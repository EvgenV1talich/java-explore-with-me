package ru.yandex.service.adminapi.compilation;

import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.dto.compilation.UpdateCompilationRequest;
import ru.yandex.model.compilation.Compilation;

public interface AdminCompilationService {

    Compilation postCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    Compilation updateCompilationById(int compId, UpdateCompilationRequest updateCompilationRequest);

}
