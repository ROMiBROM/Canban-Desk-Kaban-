package com.example.CanbanDesk.Repositories;

import com.example.CanbanDesk.Models.Comment;
import com.example.CanbanDesk.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT c FROM Comment c WHERE c.task.id = :id ORDER BY c.date DESC")
    List<Comment> findAllByTaskIdWithSortByDate(Integer id);
    @Query("SELECT c FROM Comment c WHERE c.task.id = :id")
    List<Comment> findAllByTaskId(Integer id);
}
