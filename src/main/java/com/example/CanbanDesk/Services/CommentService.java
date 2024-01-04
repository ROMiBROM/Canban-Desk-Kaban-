package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Comment;
import com.example.CanbanDesk.Repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public List<Comment> findAllCommentsByTaskIDSorted(Integer id){
        return commentRepository.findAllByTaskIdWithSortByDate(id);
    }
    public List<Comment> findAllComments(){
        return commentRepository.findAll();
    }
    public Comment findByID(Integer id){
        return commentRepository.findById(id).orElse(null);
    }
    public void SaveComment(Comment comment){
        commentRepository.save(comment);
    }
    public void DeleteComment(Comment comment){
        commentRepository.delete(comment);
    }
}
