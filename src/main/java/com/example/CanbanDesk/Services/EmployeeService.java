package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Enums.Role;
import com.example.CanbanDesk.Models.Project;
import com.example.CanbanDesk.Repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    public Employee EmployeerByEmail(String email)
    {
        return employeeRepository.findByEmail(email);
    }


    public boolean createEmployee(Employee employee)
    {
        if(employeeRepository.findByEmail(employee.getEmail())!=null) return false;
        employee.setActive(true);
        employee.getRoles().add(Role.EMPL);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        log.info("Saving new User with email-{}",employee.getEmail());
        employeeRepository.save(employee);
        return true;
    }
    public boolean updateEmployee(Employee employee)
    {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        return true;
    }
    public List<Employee> findAllEmployees()
    {
        return employeeRepository.findAll();
    }

    public void UpdateEmployee(Employee employee)
    {
        employeeRepository.save(employee);
    }
    public Employee findByID(Integer id)
    {
        return employeeRepository.findById(id).get();
    }
    public void deleteEmployee(Employee employee)
    {
        employeeRepository.delete(employee);
    };
}
