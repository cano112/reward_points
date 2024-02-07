package com.noster.rewardpoints.infra.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ExceptionHandling implements ProblemHandling {
    @Override
    public StatusType defaultConstraintViolationStatus() {
        return Status.UNPROCESSABLE_ENTITY;
    }
}