package org.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.todo.data.UserRoleMappingRepository;
@Service
public class DatabaseUserDetails implements UserDetailsService {
    @Autowired
    UserRoleMappingRepository userRoleMappingRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRoleMappingRepository.getUserDetails(username);
    }
}
