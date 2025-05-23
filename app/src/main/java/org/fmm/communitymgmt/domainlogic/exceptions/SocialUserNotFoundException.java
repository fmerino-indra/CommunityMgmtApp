package org.fmm.communitymgmt.domainlogic.exceptions;

public class SocialUserNotFoundException extends RuntimeException {
    public SocialUserNotFoundException(String message) {
        super(message);
    }

    public SocialUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
