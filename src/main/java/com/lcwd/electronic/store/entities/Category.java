package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();





}
