package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // create

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto)
    {
        CategoryDto dto = categoryService.create(categoryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // update

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String categoryId
    )
    {
        CategoryDto updated = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updated,HttpStatus.OK);
    }

    // delete

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable("categoryId") String categoryId)
    {
        categoryService.delete(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category deleted successfully")
                .status(HttpStatus.OK)
                .success(true).build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    // get all

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> allCategories = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }


    // get single

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable("categoryId") String categoryId)
    {
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }


    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file")MultipartFile file,
            @PathVariable("categoryId") String categoryId
    ) throws IOException {
        String imageName = fileService.uploadImage(file, imagePath);

        CategoryDto categoryDto = categoryService.get(categoryId);

        categoryDto.setCoverImage(imageName);

        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("image is uploaded successfully")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(
            @PathVariable("categoryId") String categoryId,
            HttpServletResponse response
    ) throws IOException {
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("Category image name : {}",categoryDto.getCoverImage());

        InputStream resource = fileService.getResource(imagePath, categoryDto.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
    }

    //search category


}
