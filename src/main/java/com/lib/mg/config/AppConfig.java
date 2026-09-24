package com.lib.mg.config;

import com.lib.mg.Dto.UserRequestDto;
import com.lib.mg.entity.UserInfo;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip mapping 'id' when mapping UserRequestDto to UserInfo to prevent
        // roleId from being loosely mapped to UserInfo.id
        modelMapper.typeMap(UserRequestDto.class, UserInfo.class)
                .addMappings(mapper -> mapper.skip(UserInfo::setId));

        return modelMapper;
    }
}

