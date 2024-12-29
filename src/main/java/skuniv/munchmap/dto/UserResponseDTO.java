package skuniv.munchmap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String token;
    private String name;

    public UserResponseDTO(String token) {
        this.token = token;
    }
}
