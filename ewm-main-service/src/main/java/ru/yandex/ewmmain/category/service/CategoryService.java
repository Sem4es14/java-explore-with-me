package ru.yandex.ewmmain.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.category.mapper.CategoryMapper;
import ru.yandex.ewmmain.category.model.Category;
import ru.yandex.ewmmain.category.repository.CategoryRepository;
import ru.yandex.ewmmain.category.requestdto.CategoryCreateRequest;
import ru.yandex.ewmmain.category.requestdto.CategoryUpdateRequest;
import ru.yandex.ewmmain.exception.model.AlreadyExists;
import ru.yandex.ewmmain.exception.model.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService{
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAll(Long from, Integer size) {
        return CategoryMapper.fromCategoryToDtos(
                categoryRepository.findAll(PageRequest.of((int) (from / size), size)).getContent()
        );
    }

    public CategoryDto getById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + categoryId + " is not found.");
        });

        return CategoryMapper.fromCategoryToDto(category);
    }

    public CategoryDto createCategory(CategoryCreateRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new AlreadyExists("Category with name " + request.getName() + " already exists");
        }
        Category category = new Category();
        category.setName(request.getName());

        return CategoryMapper.fromCategoryToDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new NotFoundException("Category with id " + categoryId + " not found");
        }

        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto updateCategory(CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(request.getId()).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + request.getId() + " is not found.");
        });
        category.setName(request.getName());

        return CategoryMapper.fromCategoryToDto(categoryRepository.save(category));
    }

}