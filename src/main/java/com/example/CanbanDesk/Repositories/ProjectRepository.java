package com.example.CanbanDesk.Repositories;

import com.example.CanbanDesk.Models.Columnn;
import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
    @Query("SELECT p FROM Project p JOIN p.employees e WHERE e.id = :employeeId")
    List<Project> findAllByEmployeeId(Integer employeeId);
    @Query("SELECT c FROM Columnn c JOIN c.project p WHERE p.id = :projectId")
    List<Columnn> findColumnsByProjectId(Integer projectId);

    @Query("SELECT e FROM Employee e JOIN e.projects p WHERE p.id = :projectId")
    List<Employee> findEmployeesByProjectId(Integer projectId);
}
