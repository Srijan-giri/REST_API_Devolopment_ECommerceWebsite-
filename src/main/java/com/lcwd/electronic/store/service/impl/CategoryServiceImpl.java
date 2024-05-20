package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String  categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto categoryDto1 = modelMapper.map(savedCategory, CategoryDto.class);

        return categoryDto1;

    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        // get category with given id
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // update category
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updateCategory = categoryRepository.save(category);

        CategoryDto dto = modelMapper.map(updateCategory, CategoryDto.class);
        return dto;
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String coverImage = category.getCoverImage();

        String fullPath = imagePath+coverImage;

        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }
        catch(Exception ex)
        {
            logger.info("user image not found in folder");
            ex.printStackTrace();
        }


        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumberer, int pageSize, String sortBy, String sortDir) {

       Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
       Pageable pageable = PageRequest.of(pageNumberer,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> response = Helper.getPagebaleResponse(page, CategoryDto.class);

        return response;


    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryDto dto = modelMapper.map(category, CategoryDto.class);

        return dto;
    }

    @Override
    public List<CategoryDto> searchCategory(String keywords) {
//        List<CategoryDto> categoryDetails = categoryRepository.findByCategoryDetails(keywords);
//        return categoryDetails;

        return null;
    }
}
