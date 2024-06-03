package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "title is required !!")
    @Size(min = 4,message = "Title must be at least 4 characters long")
    private String title;

    @NotBlank(message = "description is required !!")
    private String description;

    @NotBlank(message = "cover image is required !!")
    private String coverImage;

//   private List<ProductDto> product = new ArrayList<>();
}
