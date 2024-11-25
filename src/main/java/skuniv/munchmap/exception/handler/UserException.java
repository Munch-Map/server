package skuniv.munchmap.exception.handler;

import skuniv.munchmap.exception.GeneralException;
import skuniv.munchmap.status.BaseErrorCode;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
