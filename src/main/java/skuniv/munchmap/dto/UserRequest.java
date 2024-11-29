package skuniv.munchmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class UserRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title="USER_REQ_01 : 회원가입 요청 DTO")
    public static class userInfo {

        @Schema(description = "아이디", example = "dlatnqls921")
        private String login_id;
        @NotNull
        @Schema(description = "비밀번호", example = "sample123@")
        private String password;
        @NotNull
        @Schema(description = "이름", example = "임수빈")
        private String name;
        @NotNull
        @Schema(description = "이메일", example = "dlatnqls921@gmail.com")
        private String email;
        @NotNull
        @Schema(description = "주소(시,구,로)", example = "서울시 동대문구 장안벚꽃로 5길")
        private String address;
    }

    @Data
    @Schema(title="USER_REQ_02 : 회원가입 시 카테고리 선택 DTO")
    public static class userFavor {
        @Schema(description = "선호 카테고리 ID 리스트", example = "[1, 3, 5]")
        @JsonProperty("categoryIds") // 역직렬화 필요
        private List<Long> categoryIds;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title="USER_REQ_03 : 로그인 요청 DTO")
    public static class loginRequestDTO {
        @NotNull
        @Schema(description = "아이디", example = "dlatnqls921")
        private String login_id;
        @NotNull
        @Schema(description = "비밀번호", example = "sample123@")
        private String password;
    }

}
