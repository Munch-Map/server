package skuniv.munchmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skuniv.munchmap.status.BaseErrorCode;
import skuniv.munchmap.status.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}