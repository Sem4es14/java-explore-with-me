package ru.yandex.ewmmain.client.ewmclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.ewmmain.client.BaseClient;
import ru.yandex.ewmmain.client.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EwmClient extends BaseClient {
    @Autowired
    public EwmClient(@Value("${ewm-stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<StatsDto> get(List<String> uris) {
        String start = LocalDateTime.now().minusDays(10L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String path = "/stats?start=" + start + "&end=" + end + "&uris=" + String.join("&uris=", uris);
        ResponseEntity<Object[]> response = get(path);
        ObjectMapper mapper = new ObjectMapper();

        return Arrays.stream(response.getBody())
                .map(object -> mapper.convertValue(object, StatsDto.class))
                .collect(Collectors.toList());
    }

    public void hit(HttpServletRequest httpRequest) {
        String app = "ewm-service";
        String uri = httpRequest.getRequestURI();
        String ip = httpRequest.getRemoteAddr();

        Map<String, String> request = new HashMap<>();

        request.put("app", app);
        request.put("uri", uri);
        request.put("ip", ip);

        post("/hit", request);
    }
}
