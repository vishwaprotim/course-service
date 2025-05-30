package com.protim.course.controller;

import com.protim.course.dao.Course;
import com.protim.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public record CourseController(
        CourseService service) {


    @Operation(summary = "Get a course by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the course",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id){
        try{
            return ResponseEntity.ok(service.getById(id));
        } catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
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

    @Operation(summary = "Update a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course updated/added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) })})
    @PutMapping("/")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateCourse(course));
    }

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
        try{
            return ResponseEntity.ok(service.deleteCourse(id));
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new Course(id, ""));
        }
    }

}
