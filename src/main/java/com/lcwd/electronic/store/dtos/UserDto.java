package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    private String userId;

    @Size(min = 3,max = 20 , message = "Invalid Name !!")
    private String name;

//    @Email(message ="Invalid User Email !!" )
    @Pattern(regexp= "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message="Invalid User Email !!",flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank(message = "Email is required!!")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Size(min=3 , max=7, message ="Invalid gender !!")
    private String gender;

    @NotBlank(message="write something about yourself")
    private String about;

    //@Pattern --> Done
    // Custom validator --> Done

    @ImageNameValid
    private String imageName;
}
