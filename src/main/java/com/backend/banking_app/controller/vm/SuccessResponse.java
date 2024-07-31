package com.backend.banking_app.controller.vm;

import com.backend.banking_app.model.enumerations.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SuccessResponse {

    String message;

    int StatusCode;

    long timeStamp;

    private String status;

    public SuccessResponse(String message,int statusCode) {
        this.message = message;
        this.StatusCode = statusCode;
        this.status = String.valueOf(Status.SUCCESS);
        this.timeStamp = System.currentTimeMillis();
    }

//    200 -success
//    201 - created
//    204 - no content
//    404 - not found
//    400 - bad request
//    401 - unauthorized
//    403 - forbidden
//    500 server error
}
