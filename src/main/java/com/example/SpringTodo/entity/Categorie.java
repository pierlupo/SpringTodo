package com.example.SpringTodo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data

public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // attention erreur lazy si fetchtype pas défini sur EAGER
    @ManyToMany(mappedBy = "categories",fetch = FetchType.EAGER)
    private List<Todo> todos;
}
