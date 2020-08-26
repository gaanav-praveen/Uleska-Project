/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uleska.JobScheduler.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Gaana
 */
public class JobNotFoundError extends Exception
{
    private HttpStatus status;
    private String message;


    public JobNotFoundError(final HttpStatus status, final String message, final Throwable cause)
    {
        this.status = status;
        this.message = message;
    }

    public JobNotFoundError(final HttpStatus status, final String message)
    {
        this.status = status;
        this.message = message;
    }

}
