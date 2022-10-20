package account.user;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User userDtoToUser(UserDto userDto) {
        return User
                .builder()
                .name(userDto.getName())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles().stream().sorted().collect(Collectors.toList()))
                .build();
    }

    public UserDto userToUserDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().sorted().collect(Collectors.toList()))
                .build();
    }

    public List<UserDto> usersToUserDtos(List<User> users) {
        return users
                .stream()
                .map(this::userToUserDto)
                .collect(Collectors.toList());
    }
}