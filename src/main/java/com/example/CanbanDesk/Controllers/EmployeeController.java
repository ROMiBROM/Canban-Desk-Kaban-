package com.example.CanbanDesk.Controllers;


import com.example.CanbanDesk.Models.Comment;
import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Enums.Role;
import com.example.CanbanDesk.Models.Project;
import com.example.CanbanDesk.Models.Task;
import com.example.CanbanDesk.Services.CommentService;
import com.example.CanbanDesk.Services.EmployeeService;
import com.example.CanbanDesk.Services.ProjectService;
import com.example.CanbanDesk.Services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final CommentService commentService;
    private final TaskService taskService;
    @RequestMapping("/")
    public String Check(@RequestParam(required = false, value = "size", defaultValue = "30") Integer size)
    {
        return "Welcome";
    }
    @GetMapping("/login")
    public String Login(){
        return "Login";
    }

    @GetMapping("/loginError")
    public String LoginError(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "Login";
    }

    @GetMapping("/registration")
    public String Registration(Model model) {
        model.addAttribute("employee", new Employee());
        return "Regis";
    }
    @PostMapping("/registration")
    public String Reg(@ModelAttribute("employee") Employee employee,Model model){
        if (!employeeService.createEmployee(employee)){
            model.addAttribute("errorMessage", "Пользователь c почтой " +employee.getEmail()+ " уже существует");
            return "Regis";
        }
        log.info("Пароли совпадают {}", employee.getEmail());
        employeeService.createEmployee(employee);
        return "redirect:/login";
    }
    @GetMapping("/hello")
    public String securityUrl(@AuthenticationPrincipal UserDetails userDetails){
        Employee employee = employeeService.EmployeerByEmail(userDetails.getUsername());
        int id=employee.getId();
        log.info("Получили роль {}",userDetails.getAuthorities().toString());
        if(employee.getRoles().contains(Role.ADM))
            return "redirect:/admin";
        else if(employee.getRoles().contains(Role.EMPL))
            return "redirect:/employee";
        else
            return "redirect:/login";
    }

    @GetMapping("/admin/employees")
    @PreAuthorize("hasAuthority('ADM')")
    public String AdminEmployees(@AuthenticationPrincipal UserDetails userDetails, Model model){
        List<Employee> employees=employeeService.findAllEmployees();
        Employee Curremployee= employeeService.EmployeerByEmail(userDetails.getUsername());
        Employee user= employeeService.EmployeerByEmail(userDetails.getUsername());
        for (Employee employee:employees) {
            if(Curremployee.getId().equals(employee.getId())){
                employees.remove(employee);
                break;
            }
        }
        log.info("Получили сотрудников {}",employees.size());
        model.addAttribute("empls",employees);
        model.addAttribute("user",user);
        return "Main_admin";
    }
    @PostMapping("/invite(project{id})")
    @PreAuthorize("hasAuthority('EMPL')")
    public String invite(@RequestParam("inputEmail_invite_user") String emailInvite,@PathVariable Integer id, Model model) throws UnsupportedEncodingException {
        Project project=projectService.findByID(id);
        Employee InvEmployee=employeeService.EmployeerByEmail(emailInvite);
        if(InvEmployee==null){
            String errorMessage = "Пользователь с почтой " +emailInvite+ " не существует";
            return "redirect:/employee/project"+id+"?errorMessage=" + URLEncoder.encode(errorMessage, "UTF-8");
        }
        List<Project> InvEmployeeProjects=InvEmployee.getProjects();
        for(Project project1:InvEmployeeProjects){
            if(project1.getId().equals(id)){
                String errorMessage = "Пользователь с почтой " +emailInvite+ " уже приглашен в проект";
                return "redirect:/employee/project"+id+"?errorMessage=" + URLEncoder.encode(errorMessage, "UTF-8");
            }
        }
        InvEmployeeProjects.add(project);
        InvEmployee.setProjects(InvEmployeeProjects);
        employeeService.UpdateEmployee(InvEmployee);
        return "redirect:/employee/project"+id;
    }
    @GetMapping("/removeEmployee{id}/project{id2}")
    public String removeEmployee(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Integer id, @PathVariable Integer id2, Model model){
        Project project=projectService.findByID(id2);
        Employee employee=employeeService.findByID(id);
        List<Project> projects=employee.getProjects();
        for(Project projectt:projects){
            if(projectt.getId().equals(id2)){
                projects.remove(projectt);
                break;
            }
        }
        employee.setProjects(projects);
        employeeService.UpdateEmployee(employee);
        Employee currentEmployee = employeeService.EmployeerByEmail(userDetails.getUsername());
        Integer currentEmployeeId = currentEmployee.getId();
        return "redirect:/employee/project"+id2;
    }
    @GetMapping("/Editemployee")
    public String EditEmpl(@AuthenticationPrincipal UserDetails userDetails,@RequestParam(required = false) String errorMessage,Model model)
    {
        Employee user = employeeService.EmployeerByEmail(userDetails.getUsername());
        Employee employee = employeeService.EmployeerByEmail(userDetails.getUsername());
        if(errorMessage!=null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("employee",employee);
        model.addAttribute("user",user);
        return "UserInfo";
    }
    @GetMapping("/delEmpl")
    public String delEmplTrue(@AuthenticationPrincipal UserDetails userDetails,Model model) throws UnsupportedEncodingException {
        Employee user = employeeService.EmployeerByEmail(userDetails.getUsername());
        List<Project> projects = user.getProjects();
        List<Comment> comments = commentService.findAllComments();
        List<Task> tasks=taskService.findAll();
        for(Task t:tasks){
            if(t.getEmployee().getId().equals(user.getId())){
                String errorMessage = "Нельзя удалить аккаунт, у вас есть задачи!";
                return "redirect:/Editemployee"+"?errorMessage="+ URLEncoder.encode(errorMessage, "UTF-8");
            }
        }
        for(Project project:projects){
            if(project.getManager().getId().equals(user.getId())){
                String errorMessage = "Нельзя удалить менеджера проекта!";
                return "redirect:/Editemployee"+"?errorMessage="+ URLEncoder.encode(errorMessage, "UTF-8");
            }
        }
        for(Comment comment:comments){
            if(comment.getEmployee().getId().equals(user.getId())){
                commentService.DeleteComment(comment);
            }
        }

        for(Project project:projects){
            project.getEmployees().remove(user);
            projectService.SaveProject(project);
        }
        user.setRoles(null);
        user.setProjects(null);
        employeeService.UpdateEmployee(user);
        employeeService.deleteEmployee(user);
        return "redirect:/logout";
    }

}
