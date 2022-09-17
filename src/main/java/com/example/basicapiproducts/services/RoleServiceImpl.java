package com.example.basicapiproducts.services;

import com.example.basicapiproducts.entities.Role;
import com.example.basicapiproducts.repositories.RoleRepository;
import com.example.basicapiproducts.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
