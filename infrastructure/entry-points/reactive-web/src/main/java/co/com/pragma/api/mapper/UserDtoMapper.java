package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateUserDto;
import co.com.pragma.api.dto.EditUserDto;
import co.com.pragma.api.dto.ResponseUserDto;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    ResponseUserDto toResponseUserDto(User user);

    User toUser(CreateUserDto createUserDto);

    User toUser(EditUserDto editUserDto);

    List<ResponseUserDto> toResponseUserDtoList(List<User> users);
}