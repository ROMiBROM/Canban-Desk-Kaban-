package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Columnn;
import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.CanbanDesk.Repositories.ProjectRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    public List<Project> ProjectsByEmployeeID(Integer id)
    {
        return projectRepository.findAllByEmployeeId(id);
    }
    public List<Columnn> ColumnsByProjectID(Integer id)
    {
        return projectRepository.findColumnsByProjectId(id);
    }
    public List<Employee> EmployeesByProjectID(Integer id)
    {
        return projectRepository.findEmployeesByProjectId(id);
    }

    public List<Project> AllProjects()
    {
        return projectRepository.findAll();
    }

    public Project findByID(Integer id)
    {
        return projectRepository.findById(id).get();
    }
    public void SaveProject(Project project)
    {
        projectRepository.save(project);
    }

    public void DeleteProject(Project project)
    {
        projectRepository.delete(project);
    }
}
