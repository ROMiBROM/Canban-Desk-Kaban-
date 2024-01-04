package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Employee;
import com.example.CanbanDesk.Models.Enums.Role;
import com.example.CanbanDesk.Repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomEmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee empl=employeeRepository.findByEmail(email);
        if(empl==null){
            throw new UsernameNotFoundException("User not exists by Username");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : empl.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
        }
        for(GrantedAuthority grantedAuthority : grantedAuthorities){
            log.info("grant user " + grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(empl.getEmail(),
                empl.getPassword(),
                grantedAuthorities);
    }
}
