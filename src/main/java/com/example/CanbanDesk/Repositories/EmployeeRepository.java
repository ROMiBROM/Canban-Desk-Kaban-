package com.example.CanbanDesk.Repositories;

import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Employee findByEmail( String email);



}
