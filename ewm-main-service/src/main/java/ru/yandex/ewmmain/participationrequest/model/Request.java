package ru.yandex.ewmmain.participationrequest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}