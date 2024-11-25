package skuniv.munchmap.config.exception;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    // 2000 : Request 오류
    REQUEST_ERROR(2000, "입력값을 확인 해주세요."),
    DUPLICATE_ERROR(2001, "중복된 레코드 입니다."),
    INVALID_PWD(2002, "비밀번호가 올바르지 않습니다."),
    INVALID_USERID(2003, "유효하지 않는 USERID 입니다."),
    NOT_EXIST_ADDRESS(2006, "해당하는 주소가 없습니다."),
    DUPLICATE_USERID(2007, "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(2008, "이미 존재하는 이메일입니다."),
    INVALID_PWD_FORMAT(2009, "잘못된 비밀번호 형식입니다."),

    // 3000 : Response 오류
    RESPONSE_ERROR(3000, "값을 불러오는데 실패하였습니다."),
    INVALID_PWD_SALT(3001, "비밀번호 혹은 salt 값이 없습니다."),

    // 4000 : Database, Server 오류
    DATABASE_ERROR(4000, "데이터 베이스 접근 오류."),
    QUERY_TIMEOUT_ERROR(4001, "쿼리 타임 아웃 에러."),


    // 5000 : Server connection 오류
    SERVER_ERROR(5000, "서버와의 연결에 실패하였습니다."),
    INVALID_KAKAO_ADDRESS(5001, "카카오 주소 검색 API 호출 중 오류가 발생했습니다.");

    private final int code;
    private final String message;

    private ErrorResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
