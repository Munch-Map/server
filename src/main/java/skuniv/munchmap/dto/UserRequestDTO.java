package skuniv.munchmap.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class userInfo {
        private String loginId;
        private String password;
        private String name;
        private String email;
        private String city;
        private String district;
        private String addressDetail ;
        private String favor;
    }

    @Getter
    @Setter
    public static class login {
        private String loginId;
        private String password;
    }
}
