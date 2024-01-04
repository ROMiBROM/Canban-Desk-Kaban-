package com.example.CanbanDesk.Controllers;

import com.example.CanbanDesk.Models.*;
import com.example.CanbanDesk.Models.Enums.Role;
import com.example.CanbanDesk.Services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final ColumnService columnService;
    private final TaskService taskService;
    private final CommentService commentService;

    @GetMapping("/employee")
    @PreAuthorize("hasAuthority('EMPL')")
    public String securityUrl(@AuthenticationPrincipal UserDetails userDetails, Model model){
        Employee employee=employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer id=employee.getId();
        Employee user = employeeService.EmployeerByEmail(userDetails.getUsername());
        List<Project> projects=projectService.ProjectsByEmployeeID(id);
        log.info("Получили проекты {}",projects.size());
        model.addAttribute("projects",projects);
        model.addAttribute("user",user);
        return "hello";
    }
    @GetMapping("/employee/project{id2}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String securityUrl(@AuthenticationPrincipal UserDetails userDetails,@RequestParam(required = false) String errorMessage,@PathVariable Integer id2, Model model){
        Project project=projectService.findByID(id2);
        Employee user = employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer id=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        if(project.getManager().getId().equals(id)){
           model.addAttribute("manager",true);
        }
        else {
            model.addAttribute("manager",false);
        }
        Integer managerID=project.getManager().getId();
        List<Columnn> columns=projectService.ColumnsByProjectID(id2);
        List<Project> projects=projectService.ProjectsByEmployeeID(id);
        List<Employee> employees=projectService.EmployeesByProjectID(id2);
        List<Employee> Allemployee=employeeService.findAllEmployees();
        for (Employee employee:Allemployee){
            if(employee.getRoles().contains(Role.ADM)){
                Allemployee.remove(employee);
                break;
            }
        }
        log.info("Получили проекты {}",projects.size());
        log.info("Получили сотрудников {}",employees.size());
        log.info("Получили колонки {}",columns.size());
        if(errorMessage!=null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("columns",columns);
        model.addAttribute("projects",projects);
        model.addAttribute("projectID",id2);
        model.addAttribute("users",employees);
        model.addAttribute("managerID",managerID);
        model.addAttribute("Allemployee",Allemployee);
        model.addAttribute("user",user);
        return "hello";
    }
    @GetMapping("/employee/project{id2}/SortColumn{id3}")
    @PreAuthorize("hasAuthority('EMPL')")
    public String SortColumn(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Integer id2, @PathVariable Integer id3, Model model){
        Project project=projectService.findByID(id2);
        Integer id=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        Employee user = employeeService.EmployeerByEmail(userDetails.getUsername());
        if(project.getManager().getId().equals(id)){
            model.addAttribute("manager",true);
        }
        else {
            model.addAttribute("manager",false);
        }
        Integer managerID=project.getManager().getId();
        List<Columnn> columns=projectService.ColumnsByProjectID(id2);
        List<Project> projects=projectService.ProjectsByEmployeeID(id);
        List<Employee> employees=projectService.EmployeesByProjectID(id2);

        for(Columnn column:columns){
            if(column.getId().equals(id3)){
                column.setTasks(taskService.SortByPriorityByColumn(id3));
                break;
            }
        }
        List<Employee> Allemployee=employeeService.findAllEmployees();
        for (Employee employee:Allemployee){
            if(employee.getRoles().contains(Role.ADM)){
                Allemployee.remove(employee);
                break;
            }
        }
        log.info("Получили проекты {}",projects.size());
        log.info("Получили сотрудников {}",employees.size());
        log.info("Получили колонки {}",columns.size());
        model.addAttribute("columns",columns);
        model.addAttribute("projects",projects);
        model.addAttribute("projectID",id2);
        model.addAttribute("users",employees);
        model.addAttribute("managerID",managerID);
        model.addAttribute("Allemployee",Allemployee);
        model.addAttribute("user",user);
        return "hello";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADM')")
    public String Admin(@AuthenticationPrincipal UserDetails userDetails, Model model){
        Integer id=employeeService.EmployeerByEmail(userDetails.getUsername()).getId();
        Employee user= employeeService.EmployeerByEmail(userDetails.getUsername());
        model.addAttribute("employeeID",id);
        model.addAttribute("user",user);
        return "Main_admin";
    }


    @GetMapping("/admin/projects")
    @PreAuthorize("hasAuthority('ADM')")
    public String AdminProjects(@AuthenticationPrincipal UserDetails userDetails, Model model){
        List<Project> projects=projectService.AllProjects();
        List<Employee> employees=employeeService.findAllEmployees();
        Employee employee=employeeService.EmployeerByEmail(userDetails.getUsername());
        Employee user= employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer id=employee.getId();
        employees.remove(employee);
        model.addAttribute("projects",projects);
        model.addAttribute("employeeID",id);
        model.addAttribute("employees",employees);
        model.addAttribute("user",user);
        return "Main_admin";
    }
    @PostMapping("/createProject")
    public String CreateProject(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("manager")String managerEmail,
                                @RequestParam("description") String description, @RequestParam("name") String Projectname,
                                @RequestParam("column") String[] column, Model model){
        Project project=new Project();
        Employee currentEmployee=employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer currentEmployeeId=currentEmployee.getId();
        Employee employee=employeeService.EmployeerByEmail(managerEmail);
        List<Employee> employees=new ArrayList<>();
        employees.add(employee);
        project.setManager(employee);
        project.setName(Projectname);
        project.setDescription(description);
        projectService.SaveProject(project);
        List<Columnn>columns=new ArrayList<>();
        for(String name:column){
            Columnn columnn=new Columnn();
            columnn.setName(name);
            columnn.setProject(project);
            columnService.SaveColumn(columnn);
            columns.add(columnn);
        }
        List<Project> EmployeeProjects=employee.getProjects();
        EmployeeProjects.add(project);
        employee.setProjects(EmployeeProjects);
        employeeService.UpdateEmployee(employee);
        return "redirect:/admin/projects";
    }
    @GetMapping("/admin/deleteProject{id2}")
    @PreAuthorize("hasAuthority('ADM')")
    public String DeleteProject( @PathVariable Integer id2){
        Project project=projectService.findByID(id2);
        project.setManager(null);
        project.setEmployees(null);
        List<Employee> employees=projectService.EmployeesByProjectID(id2);
        for(Employee employee:employees){
            List<Project> projects=employee.getProjects();
            projects.remove(project);
            employee.setProjects(projects);
            employeeService.UpdateEmployee(employee);
        }
        List<Columnn> columns=projectService.ColumnsByProjectID(id2);
        for(Columnn column:columns){
            List<Task> tasks=taskService.findAllTaskBycolumnID(column.getId());
            for(Task task:tasks){
                task.setColumn(null);
                task.setEmployee(null);
                taskService.DeleteTask(task);
            }
            columnService.DeleteColumn(column);
        }
        projectService.DeleteProject(project);
        return "redirect:/admin/projects";
    }

    @GetMapping("/DataEditProject{id}")
    public ResponseEntity<String> DataEditProject(@PathVariable Integer id, Model model) {
        Project project = projectService.findByID(id);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(project);
            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/EditProject")
    public String EditProject(@RequestParam("EditprojectID") Integer id,@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam("Edit_manager")String managerEmail,
                              @RequestParam("Edit_description") String description,
                              @RequestParam("Edit_name") String Projectname,
                              @RequestParam("Edit_column") String[] column,
                              @RequestParam("Edit_column_id") Integer[] columnID, Model model)
    {
        Boolean IsInProject=false;
        Project project = projectService.findByID(id);
        Employee currentEmployee = employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer currentEmployeeId = currentEmployee.getId();
        Employee Mngemployee = employeeService.EmployeerByEmail(managerEmail);
        List<Employee> Prjemployees = project.getEmployees();
        for(Employee em:Prjemployees){
            if(em.getId().equals(Mngemployee.getId())){
                IsInProject=true;
                break;
            }
        }
        if(!IsInProject) {
            List<Project> EmployeeProjects=Mngemployee.getProjects();
            EmployeeProjects.add(project);
            Mngemployee.setProjects(EmployeeProjects);
            employeeService.UpdateEmployee(Mngemployee);
        }
        project.setManager(Mngemployee);
        project.setName(Projectname);
        project.setDescription(description);
        List<Columnn> columns=projectService.ColumnsByProjectID(id);
        for(int i=0;i<columnID.length;i++){
            Columnn columnn=new Columnn();
            columnn.setName(column[i]);
            columnn.setProject(project);
            columnService.SaveColumn(columnn);
            for(Columnn columnN:columns){
                if(columnN.getId().equals(columnID[i])){
                    List<Task> tasks=taskService.findAllTaskBycolumnID(columnN.getId());
                    for(Task task:tasks){
                        task.setColumn(columnn);
                        taskService.SaveTask(task);
                    }
                    break;
                }
            }

        }
        for(Columnn columnN:columns){
            List<Task> tasks=taskService.findAllTaskBycolumnID(columnN.getId());
            for(Task task:tasks){
                task.setColumn(null);
                task.setEmployee(null);
                taskService.DeleteTask(task);
                tasks.remove(task);
            }
            columnService.DeleteColumn(columnN);
        }
        return "redirect:/admin/projects";
    }

}
