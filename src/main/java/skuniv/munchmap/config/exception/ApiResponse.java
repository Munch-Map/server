package skuniv.munchmap.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final int status;


    // 성공
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, HttpStatus.OK.value());
    }

    // 에러
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static <T> ApiResponse<T> errorWithStatus(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, null, status.value());
    }

}
