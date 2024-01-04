package com.example.CanbanDesk.Repositories;

import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    @Query("SELECT t FROM Task t JOIN t.column c WHERE c.id = :columnId")
    List<Task> findAllByColumnId(Integer columnId);

}
