package ru.yandex.ewmstatsservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.ewmstatsservice.mapper.StatsMapper;
import ru.yandex.ewmstatsservice.model.Stats;
import ru.yandex.ewmstatsservice.repository.StatsRepository;
import ru.yandex.ewmstatsservice.requestdto.StatsRequest;
import ru.yandex.ewmstatsservice.responsedto.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    public void hit(StatsRequest request) {
        Stats stats = new Stats();
        stats.setApp(request.getApp());
        stats.setIp(request.getIp());
        stats.setUri(request.getUri());
        stats.setDatetime(LocalDateTime.now());

        statsRepository.save(stats);
    }

    public List<StatsResponse> get(String startStr, String endStr, List<String> urls, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);

        return unique ?
                StatsMapper.fromDbToStatsResponses(statsRepository.countUniq(start, end, urls.toArray(new String[0]))) :
                StatsMapper.fromDbToStatsResponses(statsRepository.count(start, end, urls.toArray(new String[0])));

    }
}
