package com.sahinokdem.researcher_metadata.enums;

public enum State {
    Q0, // Ready to send form
    Q1, // Sent form, Ready to send CV
    Q2, // Rejected first cv, Ready to send second CV

    State()
}
