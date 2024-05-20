package com.lcwd.electronic.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name="category_id")
    private String categoryId;
    @Column(name="category_title",length = 60,nullable = false)
    private String title;

    @Column(name="category_desc",length=100)
    private String description;

    private String coverImage;


    // other attributes if you have


}
