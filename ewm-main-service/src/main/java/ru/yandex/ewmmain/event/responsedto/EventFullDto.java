package ru.yandex.ewmmain.event.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.comments.responsedto.CommentDto;
import ru.yandex.ewmmain.event.model.EventState;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String description;
    private String annotation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean paid;
    private EventState state;
    private Boolean requestModeration;
    private Integer participantLimit;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long views = 0L;
    private Long confirmedRequests = 0L;
    private Location location;
    private List<CommentDto> comments;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private float lat;
        private float lon;
    }
}