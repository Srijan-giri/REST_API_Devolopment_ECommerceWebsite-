package com.lcwd.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


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
}
