package com.example.CanbanDesk.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

import java.util.ArrayList;

@Entity
@Table(name="Project")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Integer id;
    @Column(name="project_name")
    private String name;
    @Column(name="project_description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private Employee manager;
    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees = new ArrayList<>();
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Columnn> columns = new ArrayList<>();
}
