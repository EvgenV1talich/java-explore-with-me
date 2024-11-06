package ru.yandex.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.dto.event.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Integer id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;

}
