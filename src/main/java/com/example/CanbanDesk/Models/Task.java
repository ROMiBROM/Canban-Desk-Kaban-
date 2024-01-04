package com.example.CanbanDesk.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private Integer id;
    @Column(name="task_title")
    private String title;
    @Column(name="task_body")
    private String body;
    @Column(name="task_deadline")
    private LocalDate deadline;
    @Column(name="task_priority")
    private String priority;
    @ManyToOne
    @JoinColumn(name = "task_employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "task_column_id") // указываем имя столбца, который используется для связи
    private Columnn column;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
