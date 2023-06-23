package com.example.SpringTodo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class TodoDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private LocalDate dueDate;

    private int priority;

    @OneToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

}
