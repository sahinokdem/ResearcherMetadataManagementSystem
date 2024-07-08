package com.sahinokdem.researcher_metadata.mapper;



import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.model.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
