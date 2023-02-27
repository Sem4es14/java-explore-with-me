package ru.yandex.ewmmain.compilations.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}