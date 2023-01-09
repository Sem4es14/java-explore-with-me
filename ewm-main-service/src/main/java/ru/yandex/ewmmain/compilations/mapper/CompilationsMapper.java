package ru.yandex.ewmmain.compilations.mapper;

import ru.yandex.ewmmain.client.ewmclient.EwmClient;
import ru.yandex.ewmmain.compilations.model.Compilation;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.event.mapper.EventMapper;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationsMapper {
    public static CompilationDto fromCompilationToDto(Compilation compilation,
                                                      EwmClient ewmClient,
                                                      RequestRepository requestRepository) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                compilation.getEvents().stream()
                        .map(event -> EventMapper.fromEventToShortDto(event, ewmClient, requestRepository))
                        .collect(Collectors.toList())
        );
    }

    public static List<CompilationDto> fromCompilationsToDtos(List<Compilation> compilations,
                                                              EwmClient ewmClient,
                                                              RequestRepository requestRepository) {
        return compilations.stream()
                .map(compilation -> CompilationsMapper.fromCompilationToDto(compilation, ewmClient, requestRepository))
                .collect(Collectors.toList());
    }
}
