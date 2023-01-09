package ru.yandex.ewmstatsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewmstatsservice.model.Stats;

import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query(value = "SELECT COUNT(s.ip) FROM Stats AS s " +
            "WHERE s.datetime between :start and :end and " +
            "      s.uri = :url")
    Long count(LocalDateTime start, LocalDateTime end, String url);

    @Query(value = "SELECT COUNT(DISTINCT(s.ip)) FROM Stats AS s " +
            "WHERE s.datetime between :start and :end and " +
            "      s.uri = :url ")
    Long countUniq(LocalDateTime start, LocalDateTime end, String url);
}
