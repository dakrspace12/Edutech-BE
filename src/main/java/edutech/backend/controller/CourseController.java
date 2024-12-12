package edutech.backend.controller;

import edutech.backend.entity.Course;
import edutech.backend.exception.CourseNotFoundException;
import edutech.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();  //calls all courses
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.findCourseById(id); // Calls the method by their ID
            return ResponseEntity.ok(course);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Handle the exception
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createCourse(@RequestBody Course course) {
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body("Course: "+savedCourse+" added successfully"); // 201 Created
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String>deleteCourse(@PathVariable Long id){
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course with ID "+id+" Deleted successfully");
        }catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Unexpected error occurred.");
        }
    }
}
