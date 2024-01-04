package com.example.CanbanDesk.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Columnn")
@Data
public class Columnn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="column_id")
    private Integer id;
    @Column(name="column_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "column_project_id")
    @JsonIgnore
    private Project project;
    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();
}
