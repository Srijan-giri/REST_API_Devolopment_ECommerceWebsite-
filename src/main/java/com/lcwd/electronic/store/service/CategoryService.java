package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    // create

    CategoryDto create(CategoryDto categoryDto);

    // update

    CategoryDto update(CategoryDto categoryDto,String categoryId);
    // delete

    void delete(String categoryId);

    // get all

    PageableResponse<CategoryDto> getAll(int pageNumberer, int pageSize, String sortBy, String sortDir);

    // get single category details
    CategoryDto get(String categoryId);

    // serach:

    List<CategoryDto> searchCategory(String keywords);




}
