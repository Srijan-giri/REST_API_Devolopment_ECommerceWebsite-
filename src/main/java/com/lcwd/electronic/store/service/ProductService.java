package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {

    // create

    ProductDto create(ProductDto productDto);

    // update

    ProductDto update(ProductDto productDto,String productId);

    // delete

    void delete(String productId);

    // get single

    ProductDto get(String productId);

    // get all

    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get all : like

    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);


    // search product

    PageableResponse<ProductDto> search(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);
}
