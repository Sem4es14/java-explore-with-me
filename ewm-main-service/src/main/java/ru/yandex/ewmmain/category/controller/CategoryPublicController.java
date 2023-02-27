package ru.yandex.ewmmain.category.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(value = "from", defaultValue = "0")
                                                    @PositiveOrZero Long from,
                                                    @RequestParam(value = "size", defaultValue = "10")
                                                    @Positive Integer size) {

        return ResponseEntity.of(Optional.of(categoryService.getAll(from, size)));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable("catId") @Positive Long categoryId) {
        return ResponseEntity.of(Optional.of(categoryService.getById(categoryId)));
    }
}