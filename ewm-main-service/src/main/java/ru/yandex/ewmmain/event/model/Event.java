package ru.yandex.ewmmain.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmmain.category.model.Category;
import ru.yandex.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn = null;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "request_moderation")
    private Boolean requestModeration = true;

    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "initiator")
    private User initiator;

    @Column(name = "latitude")
    private Float lat;

    @Column(name = "longitude")
    private Float lon;
}