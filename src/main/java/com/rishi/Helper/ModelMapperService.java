package com.rishi.Helper;


import com.rishi.dto.UserProfileDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


@Data
public class ModelMapperService {

    @Autowired
    private  ModelMapper modelMapper;

    public ModelMapperService() {

    }

    public ModelMapperService(org.modelmapper.ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserProfileDTO entityToDto(UserProfileDTO saveedUser) {
        return modelMapper.map(saveedUser, UserProfileDTO.class);
    }

    public UserProfileDTO dtoToEntity(UserProfileDTO userDTO) {
        return modelMapper.map(userDTO, UserProfileDTO.class);
    }

}
