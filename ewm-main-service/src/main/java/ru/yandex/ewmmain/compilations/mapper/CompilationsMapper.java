package ru.yandex.ewmmain.compilations.mapper;

import ru.yandex.ewmmain.compilations.model.Compilation;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationsMapper {
    public static CompilationDto fromCompilationToDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                compilation.getEvents().stream()
                        .map(EventMapper::fromEventToShortDto)
                        .collect(Collectors.toList())
        );
    }

    public static List<CompilationDto> fromCompilationsToDtos(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationsMapper::fromCompilationToDto)
                .collect(Collectors.toList());
    }
}
