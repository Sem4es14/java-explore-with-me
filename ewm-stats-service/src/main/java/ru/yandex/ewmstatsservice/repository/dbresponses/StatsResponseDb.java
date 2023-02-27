package ru.yandex.ewmstatsservice.repository.dbresponses;

public interface StatsResponseDb {
    String getApp();

    String getUri();

    Long getHits();
}
