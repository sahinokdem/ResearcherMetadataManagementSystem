package com.sahinokdem.researcher_metadata.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReviewRequest {
    @NotEmpty
    private boolean isAccepted;

    private String reason;
}
