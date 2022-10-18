package account.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User userDtoToUser(UserDto userDto) {
        return User
                .builder()
                .name(userDto.getName())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public UserDto userToUserDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}