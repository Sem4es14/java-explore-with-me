package ru.yandex.ewmmain.client;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.ewmmain.client.dto.StatsDto;

import java.util.List;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
        this.rest = rest;
    }

    protected ResponseEntity<Object[]> get(String path) {
        return makeAndSendGet(HttpMethod.GET, path);
    }

    protected <T> ResponseEntity<StatsDto> post(String path, T body) {
        return makeAndSendPost(HttpMethod.POST, path, body);
    }

    private ResponseEntity<Object[]> makeAndSendGet(HttpMethod method, String path) {
        var requestEntity = new HttpEntity<>(defaultHeaders());

        ResponseEntity<Object[]> statsServerResponse;

        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, Object[].class);
        } catch (HttpStatusCodeException e) {
            throw new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {
                @Override
                public HttpStatus getStatusCode() {
                    return super.getStatusCode();
                }
            };
        }

        return prepareResponse(statsServerResponse);
    }

    private <T> ResponseEntity<StatsDto> makeAndSendPost(HttpMethod method, String path,
                                                         @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<StatsDto> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, StatsDto.class);
        } catch (HttpStatusCodeException e) {
            throw new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {
                @Override
                public HttpStatus getStatusCode() {
                    return super.getStatusCode();
                }
            };
        }
        return prepareResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static <T> ResponseEntity<T> prepareResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}