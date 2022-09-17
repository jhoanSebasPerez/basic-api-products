package com.example.basicapiproducts.services;

import com.example.basicapiproducts.dtos.UserDto;
import com.example.basicapiproducts.entities.Role;
import com.example.basicapiproducts.entities.User;
import com.example.basicapiproducts.exceptions.UserAlreadyExistException;
import com.example.basicapiproducts.repositories.UserRepository;
import com.example.basicapiproducts.services.interfaces.AuthService;
import com.example.basicapiproducts.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AuthServiceImple implements UserDetailsService, AuthService {

    private UserRepository userRepository;

    private RoleServiceImpl roleService;

    private PasswordEncoder passwordEncoder;

    public AuthServiceImple(UserRepository userRepository,
                            RoleServiceImpl roleService,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username );
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Not found user with that username!");
        }

        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public User save(UserDto userDto) {
        User user = userDto.fromDtoToUser();

        if(userRepository.existsByEmail(user.getEmail()))
            throw new UserAlreadyExistException("User already exist with that email");

        if (userRepository.existsByUsername(user.getUsername()))
            throw new UserAlreadyExistException("User already exist with that username");

        Set<Role> roles = new HashSet<>();
        Role userRole = roleService.findByName("USER");
        roles.add(userRole);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void becomeToUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new NoSuchElementException("Not found user with that username");
        }

        Role adminRole = roleService.findByName("ADMIN");
        user.getRoles().add(adminRole);
        userRepository.save(user);
    }


}
