package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String productId;

    @NotBlank(message = "tittle is required !!")
    @Size(min=2,message = "Title must be at least 4 characters long")
    private String title;

    @NotBlank(message = "description is required !!")
    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private LocalDateTime addedDate;

    private boolean live;

    private boolean stock;

    @ImageNameValid
    private String productImageName;
}
