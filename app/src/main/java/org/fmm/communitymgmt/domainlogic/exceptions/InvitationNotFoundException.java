package org.fmm.communitymgmt.domainlogic.exceptions;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(String message) {
        super(message);
    }

    public InvitationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
