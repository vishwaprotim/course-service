package com.protim.course.controller;

import com.protim.course.dao.Course;
import com.protim.course.dao.ExceptionResponse;
import com.protim.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private static final String COURSE_CACHE = "courseCache";

    @Autowired
    CourseService service;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = KeyAlreadyExistsException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExceptionResponse handleDuplicateKeyException(Exception e){
        log.error("Exception in Controller", e);
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, "Course already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(exception = IllegalArgumentException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExceptionResponse handleNotFoundException(Exception e){
        log.error("Exception in Controller", e);
        return new ExceptionResponse(HttpStatus.NOT_FOUND, "Cannot find record with this id");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = UnsupportedOperationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExceptionResponse handleBadRequestException(Exception e){
        log.error("Exception in Controller", e);
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, "Cannot remove. Course does not exist");
    }

    @Cacheable(value=COURSE_CACHE)
    @Operation(summary = "Get a course by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the course",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id){
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all courses",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) })})
    @GetMapping("/")
    public ResponseEntity<List<Course>> getCourses(){
        return ResponseEntity.ok(service.getAllCourses());
    }

    @CachePut(value = COURSE_CACHE)
    @Operation(summary = "Add a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) })})
    @PostMapping("/")
    public ResponseEntity<Course> addCourse(@RequestBody Course course){
        return ResponseEntity.created(URI.create("/" + course.courseId()))
                .body(service.addCourse(course));
    }

    @CachePut(value = COURSE_CACHE)
    @Operation(summary = "Update a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course updated/added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) })})
    @PutMapping("/")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateCourse(course));
    }

    @CacheEvict(value = COURSE_CACHE)
    @Operation(summary = "Remove a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course removed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid course id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id){
        return ResponseEntity.ok(service.deleteCourse(id));
    }

}
