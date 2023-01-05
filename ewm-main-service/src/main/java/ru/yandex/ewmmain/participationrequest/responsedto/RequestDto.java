package ru.yandex.ewmmain.participationrequest.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private RequestStatus status;
    private LocalDateTime created;
    private Long requester;
    private Long event;
}