package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.service.ProductService;
import org.modelmapper.ModelMapper;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String imagePath;
    
    @Override
    public ProductDto create(ProductDto productDto) {
        String id = UUID.randomUUID().toString();
        productDto.setProductId(id);
        productDto.setAddedDate(LocalDateTime.now());

        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        ProductDto dto = modelMapper.map(savedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        // fetch the product of given id
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.isLive());
        product.setQuantity(productDto.getQuantity());
//        product.setAddedDate(productDto.getAddedDate());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        Product savedProduct = productRepository.save(product);
        ProductDto dto = modelMapper.map(savedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        String imageName = product.getProductImageName();
        String fullPath = imagePath+imageName;

        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        ProductDto dto = modelMapper.map(product, ProductDto.class);

        return dto;
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findAll(pageable);
        PageableResponse<ProductDto> responseDto = Helper.getPagebaleResponse(products, ProductDto.class);
        return responseDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> list = productRepository.findByLiveTrue(pageable);
        return Helper.getPagebaleResponse(list, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> search(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> list = productRepository.findByTitleContaining(subTitle,pageable);
        return Helper.getPagebaleResponse(list, ProductDto.class);
    }
}
