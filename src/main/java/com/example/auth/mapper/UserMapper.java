package com.example.auth.mapper;

import com.example.auth.dto.user.UpdateUserResponse;
import com.example.auth.dto.user.UserDetailsDto;
import com.example.auth.dto.user.UserInfoDto;
import com.example.auth.dto.user.UserInfoEditRequest;
import com.example.auth.entity.PhoneNumber;
import com.example.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PhoneNumberMapper.class)
public interface UserMapper {

    UserInfoDto mapToUserDto(User users);

    @Mapping(target = "phoneNumbers", source = "phoneNumbers")
    UpdateUserResponse mapToUpdateUserResponse(User users);

    UserDetailsDto mapToUserDetails(User user);

    @Mapping(target = "phoneNumbers", ignore = true)
    User mapUserInfoEditRequestToUser(UserInfoEditRequest request, @MappingTarget User user);

    default Page<UserInfoDto> mapToUserDto(Page<User> users) {
        return users.map(this::mapToUserDto);
    }

    default Page<UserDetailsDto> mapToUserDetails(Page<User> users) {
        return users.map(this::mapToUserDetails);
    }

    default List<String> mapPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .map(PhoneNumber::getPhoneNumber)
                .collect(Collectors.toList());
    }

}
