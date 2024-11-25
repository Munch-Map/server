package skuniv.munchmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int status;

    private ApiResponse(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }

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
