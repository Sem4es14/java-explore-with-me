package ru.yandex.ewmstatsservice.mapper;

import ru.yandex.ewmstatsservice.repository.dbresponses.StatsResponseDb;
import ru.yandex.ewmstatsservice.responsedto.StatsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {
    public static StatsResponse fromDbToStatsResponse(StatsResponseDb responseDb) {
        return new StatsResponse(
                responseDb.getApp(),
                responseDb.getUri(),
                responseDb.getHits()
        );
    }

    public static List<StatsResponse> fromDbToStatsResponses(List<StatsResponseDb> statsResponseDbs) {
        return statsResponseDbs.stream()
                .map(StatsMapper::fromDbToStatsResponse)
                .collect(Collectors.toList());
    }
}
