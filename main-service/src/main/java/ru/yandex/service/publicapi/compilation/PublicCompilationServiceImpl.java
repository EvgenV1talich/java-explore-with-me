package ru.yandex.service.publicapi.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.repository.CompilationRepository;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository repository;

    @Override
    public List<Compilation> getCompilations(Boolean pinned) {
        log.info(MessageFormat
                .format("Getting compilation list pinned= {0}", pinned));

        if (pinned != null) {
            return repository.findAllByPinned(pinned);
        } else {
            return repository.findAll();
        }
    }

    @Override
    public Compilation getCompilationById(long compilationId) {
        log.info(MessageFormat
                .format("Searching Compilation id={0}", compilationId));

        return repository.findById(compilationId).orElseThrow(() -> new NotFoundException(MessageFormat
                .format("Compilation not found with id = {0}", compilationId)));
    }

}
