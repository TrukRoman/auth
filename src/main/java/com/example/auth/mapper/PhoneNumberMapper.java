package com.example.auth.mapper;

import com.example.auth.entity.PhoneNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {

    String mapPhoneNumber(PhoneNumber phoneNumber);
}

