package com.rental.manager.service.queue;

public enum EmailTaskType {
    EMAIL_VERIFICATION,
    OWNER_CREDENTIALS,
    RESEND_OWNER_CREDENTIALS,
    FORGOT_PASSWORD,
    BOOKING_INFO_TO_GUEST,
    BOOKING_INFO_TO_OWNER
}
