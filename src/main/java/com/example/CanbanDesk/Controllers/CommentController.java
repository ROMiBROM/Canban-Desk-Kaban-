package com.example.CanbanDesk.Controllers;

import com.example.CanbanDesk.Models.Comment;
import com.example.CanbanDesk.Services.CommentService;
import com.example.CanbanDesk.Services.EmployeeService;
import com.example.CanbanDesk.Services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final EmployeeService employeeService;
    private final TaskService taskService;
    @PostMapping("/createComment/task{id}/prj{id2}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String AddComment(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("body") String body,
                             @PathVariable Integer id,
                             @PathVariable Integer id2, Model model) {
        Comment comment = new Comment();
        comment.setBody(body);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        comment.setDate(date);
        comment.setEmployee(employeeService.EmployeerByEmail(userDetails.getUsername()));
        comment.setTask(taskService.findByID(id));
        commentService.SaveComment(comment);
        Integer emplID=comment.getEmployee().getId();
        return "redirect:/employee/project" + id2 + "/task" + id;

    }
    @GetMapping("/deleteComment{id}/task{id2}/prj{id3}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String DeleteComment(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Integer id, @PathVariable Integer id2, @PathVariable Integer id3) {
        Comment comment = commentService.findByID(id);
        commentService.DeleteComment(comment);
        Integer emplID=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        return "redirect:/employee/project" + id3 + "/task" + id2;
    }

    @PostMapping("/editComment{id}/task{id2}/prj{id3}")
    public String EditComment(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam("body") String body,
                              @PathVariable Integer id,
                              @PathVariable Integer id2,
                              @PathVariable Integer id3)
    {
        Comment comment = commentService.findByID(id);
        comment.setBody(body);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        comment.setDate(date);
        commentService.SaveComment(comment);
        Integer emplID=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        return "redirect:/employee/project" + id3 + "/task" + id2;
    }
}
