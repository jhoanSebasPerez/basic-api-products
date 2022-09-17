package com.example.basicapiproducts.services.interfaces;

import com.example.basicapiproducts.dtos.UserDto;
import com.example.basicapiproducts.entities.User;

public interface AuthService {

    User save(UserDto userDto);

    void becomeToUser(String username);
}
