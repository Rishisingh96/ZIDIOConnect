package com.rishi.service.impl;

import com.rishi.dto.UserDTO;
import com.rishi.entity.User;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rishi.service.ModelMapperService;

@Service
@RequiredArgsConstructor
public class ModelMapperServiceImpl implements ModelMapperService{

    private final ModelMapper modelMapper;

    @Override
    public UserDTO entityToDto(User saveedUser) {
        return modelMapper.map(saveedUser, UserDTO.class);
    }

    @Override
    public User dtoToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


}
