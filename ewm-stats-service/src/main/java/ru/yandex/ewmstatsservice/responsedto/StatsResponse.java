package ru.yandex.ewmstatsservice.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
    private String app;
    private String uri;
    private Long hits;
}
