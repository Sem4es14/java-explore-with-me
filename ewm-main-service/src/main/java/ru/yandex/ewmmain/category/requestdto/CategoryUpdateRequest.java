package ru.yandex.ewmmain.category.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {
    @NotNull
    @Positive
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
}