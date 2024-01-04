package com.example.CanbanDesk.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="Comment")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Integer id;
    @Column(name="comment_body")
    private String body;
    @Column(name="comment_time")
    private Date date;
    @ManyToOne
    @JoinColumn(name="comment_employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name="comment_task_id")
    private Task task;
    private String dateFormatter;
}
