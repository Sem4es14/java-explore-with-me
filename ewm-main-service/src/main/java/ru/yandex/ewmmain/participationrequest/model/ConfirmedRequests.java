package ru.yandex.ewmmain.participationrequest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfirmedRequests {
    @Column(name = "event_id")
    Long eventId;
    @Column(name = "count")
    Long count;
}
