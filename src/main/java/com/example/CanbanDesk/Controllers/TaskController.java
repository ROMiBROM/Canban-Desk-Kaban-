package com.example.CanbanDesk.Controllers;

import com.example.CanbanDesk.Models.*;
import com.example.CanbanDesk.Services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController{
    private final TaskService taskService;
    private final ColumnService columnService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final CommentService commentService;
    @PostMapping("/CreateTask(project{id}column)")
    @PreAuthorize("hasAuthority('EMPL')")
    public String CreateTask(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("name_task")String name, @RequestParam("description_task") String description,
                             @RequestParam("busyEmployee")String busyEmployeeEmail, @RequestParam("priority") String priority,
                             @RequestParam("Date_task")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Date_task, @PathVariable Integer id,
                             @RequestParam("columnID") Integer id2, Model model) throws UnsupportedEncodingException {
        Employee employee= employeeService.EmployeerByEmail(userDetails.getUsername());
        Task task=new Task();
        task.setTitle(name);
        task.setBody(description);
        LocalDate Date_t = Date_task.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        String errorMessage = "Некорректная дата: Она должна быть после сегодняшнего дня";
        if(!Date_t.isAfter(currentDate)){
            model.addAttribute("errorMessage","Некорректная дата: Она должна быть после сегодняшнего дня");
            return "redirect:/employee/project"+id+"?errorMessage=" + URLEncoder.encode(errorMessage, "UTF-8");
        }
        task.setDeadline(Date_t);
        task.setPriority(priority);
        task.setEmployee(employeeService.EmployeerByEmail(busyEmployeeEmail));
        task.setColumn(columnService.findByID(id2));
        taskService.SaveTask(task);
        return "redirect:/employee/project"+id;
    }
    @PostMapping("/MoveTask")
    @PreAuthorize("hasAuthority('EMPL')")
    public ResponseEntity<String> MoveTask(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request, Model model)
    {
        Integer id = Integer.parseInt(request.getParameter("taskId"));
        Integer ColNewid = Integer.parseInt(request.getParameter("columnNewId"));
        Employee employee= employeeService.EmployeerByEmail(userDetails.getUsername());
        Task task=taskService.findByID(id);
        Columnn Oldcolumnn = task.getColumn();
        Columnn columnn = columnService.findByID(ColNewid);
        task.setColumn(columnn);
        taskService.SaveTask(task);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/employee/prj{id2}/DeleteTask{id3}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String DeleteTask( @PathVariable Integer id2,@PathVariable Integer id3, Model model)
    {
        Task task=taskService.findByID(id3);
        taskService.DeleteTask(task);
        return "redirect:/employee/project"+id2;
    }
    @GetMapping("/employee/prj{id2}/DataEditTask{id3}")
    @PreAuthorize("hasAuthority('EMPL')")
    public ResponseEntity<String> DataEditTask( @PathVariable Integer id2, @PathVariable Integer id3, Model model) {
        Task task = taskService.findByID(id3);
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
            String jsonString = objectMapper.writeValueAsString(task);
            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/employee/prj{id2}/EditTask")
    @PreAuthorize("hasAuthority('EMPL')")
    public String EditTask( @PathVariable Integer id2,
                           @RequestParam("Editname_task")String name, @RequestParam("Editdescription_task") String description,
                           @RequestParam("EditbusyEmployee")String busyEmployeeEmail, @RequestParam("Editpriority") String priority,
                           @RequestParam("EditDate_task")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Date_task,
                           @RequestParam("EditcolumnID") Integer colID,@RequestParam("EdittaskID") Integer TaskID ,Model model) throws UnsupportedEncodingException {
        Task task = taskService.findByID(TaskID);
        task.setTitle(name);
        task.setBody(description);
        LocalDate Date_t = Date_task.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        String errorMessage = "Некорректная дата: Она должна быть после сегодняшнего дня";
        if(!Date_t.isAfter(currentDate)){
            return "redirect:/employee/project"+id2+"?errorMessage=" + URLEncoder.encode(errorMessage, "UTF-8");
        }
        task.setDeadline(Date_t);
        task.setPriority(priority);
        task.setEmployee(employeeService.EmployeerByEmail(busyEmployeeEmail));
        task.setColumn(columnService.findByID(colID));
        taskService.SaveTask(task);
        return "redirect:/employee/project"+id2;
    }
    @GetMapping("/employee/project{id2}/task{id3}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String Task( @AuthenticationPrincipal UserDetails userDetails,@PathVariable Integer id2, @PathVariable Integer id3, Model model) {
        Task task = taskService.findByID(id3);
        Integer id=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        Employee user= employeeService.EmployeerByEmail(userDetails.getUsername());
        List<Project> projects=projectService.ProjectsByEmployeeID(id);
        List<Comment> comments=commentService.findAllCommentsByTaskIDSorted(id3);
        List<Employee> employees = projectService.findByID(id2).getEmployees();
        for(Employee employee:employees){
            if(employee.getId().equals(task.getEmployee().getId())){
                model.addAttribute("employee",employee);
                break;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for(Comment comment:comments) {
            comment.setDateFormatter(formatter.format(comment.getDate()));
        }
        model.addAttribute("comments",comments);
        model.addAttribute("task", task);
        model.addAttribute("projects",projects);
        model.addAttribute("employeeID",id);
        model.addAttribute("projectID",id2);
        model.addAttribute("comments",comments);
        model.addAttribute("user",user);
        return "TaskInfo";
    }

}
