package ru.yandex.ewmstatsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewmstatsservice.model.Stats;
import ru.yandex.ewmstatsservice.repository.dbresponses.StatsResponseDb;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query(value = "SELECT COUNT(s.ip) as hits, s.uri, s.app FROM stats AS s " +
            "WHERE s.datetime between :start and :end and " +
            "      s.uri in (:uris) " +
            "group by s.uri, s.app ", nativeQuery = true)
    List<StatsResponseDb> count(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "SELECT COUNT(DISTINCT(s.ip)) as hits, uri, app FROM stats AS s " +
            "WHERE s.datetime between :start and :end and " +
            "      s.uri in (:uris) " +
            "group by uri, app ", nativeQuery = true)
    List<StatsResponseDb> countUniq(LocalDateTime start, LocalDateTime end, String[] uris);
}