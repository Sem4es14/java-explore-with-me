package ru.yandex.ewmmain.category.mapper;

import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto fromCategoryToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryDto> fromCategoryToDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::fromCategoryToDto)
                .collect(Collectors.toList());
    }
}