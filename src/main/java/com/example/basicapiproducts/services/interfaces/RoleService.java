package com.example.basicapiproducts.services.interfaces;

import com.example.basicapiproducts.entities.Role;

public interface RoleService {

    Role findByName(String name);
}
