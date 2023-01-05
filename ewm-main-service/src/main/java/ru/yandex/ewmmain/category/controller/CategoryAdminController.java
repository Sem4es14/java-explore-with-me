package ru.yandex.ewmmain.category.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.category.requestdto.CategoryCreateRequest;
import ru.yandex.ewmmain.category.requestdto.CategoryUpdateRequest;
import ru.yandex.ewmmain.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/categories")
@Validated
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.of(Optional.of(categoryService.createCategory(request)));
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryUpdateRequest request) {
        return ResponseEntity.of(Optional.of(categoryService.updateCategory(request)));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") @Positive Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}