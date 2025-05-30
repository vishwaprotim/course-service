package com.protim.course.service;

import com.protim.course.dao.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    List<Course> courseRepository = new ArrayList<>();

    public Course getById(int id){
        return courseRepository.stream()
                .filter(c -> c.courseId() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Not Found!"));
    }

    public List<Course> getAllCourses(){
        return courseRepository;
    }

    public Course addCourse(Course course){
        courseRepository.add(course);
        return course;
    }

    public Course deleteCourse(int id){
        Course course = getById(id);
        courseRepository = courseRepository.stream()
                .filter(c -> c.courseId() != id).collect(Collectors.toList());
        return course;
    }

    public Course updateCourse(Course c){
        try{
            deleteCourse(c.courseId());
        } catch (IllegalArgumentException e){
            log.error("Course not found. Inserting a new record.");
        }
        courseRepository.add(c);
        return c;
    }
}
