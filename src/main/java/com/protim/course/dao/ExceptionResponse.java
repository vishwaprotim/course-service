package com.protim.course.dao;

import org.springframework.http.HttpStatus;

public record ExceptionResponse (HttpStatus status, String message) {}
