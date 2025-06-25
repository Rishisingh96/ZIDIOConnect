package com.rishi.service;

import com.rishi.dto.UserDTO;
import com.rishi.entity.User;

public interface ModelMapperService {

    UserDTO entityToDto(User saveedUser);

    User dtoToEntity(UserDTO userDTO);
}
