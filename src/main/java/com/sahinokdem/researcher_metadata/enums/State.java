package com.sahinokdem.researcher_metadata.enums;

public enum State {
    SEND_FORM, // Ready to send form
    SEND_CV, // Sent form, Ready to send CV
    SEND_SECOND_CV, // Rejected first cv, ready to second
    SECOND_CV_SENT, // Second cv is sent
}
