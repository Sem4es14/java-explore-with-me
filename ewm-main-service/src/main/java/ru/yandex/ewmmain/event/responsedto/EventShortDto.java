package ru.yandex.ewmmain.event.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmmain.category.model.Category;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long views = 0L;
    private Long confirmedRequests = 0L;
}