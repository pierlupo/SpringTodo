package com.example.SpringTodo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name= "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private boolean completed;

    @OneToOne(mappedBy ="todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private TodoDetail todoDetail;

    @ManyToOne
    private Personne personne;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "todo_categorie",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private List<Categorie> categories;
}
