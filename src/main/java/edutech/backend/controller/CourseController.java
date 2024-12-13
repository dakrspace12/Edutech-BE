package edutech.backend.controller;

import edutech.backend.entity.Course;
import edutech.backend.exception.CourseNotFoundException;
import edutech.backend.service.CourseService;
import edutech.backend.util.MessageConstant;
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
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.findCourseById(id);
            return ResponseEntity.ok(course);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hashRole('ADMIN')")
    public ResponseEntity<String> updateCourse(@PathVariable Long id,@RequestBody Course updatedCourse){
        try {
            Course update = courseService.updateCourse(id,updatedCourse);
            return ResponseEntity.status(HttpStatus.OK).body(MessageConstant.COURSE_WITH_ID+id+MessageConstant.UPDATED+MessageConstant.SUCCESSFULLY);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageConstant.AN_UNEXPECTED_ERROR_OCCURRED);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String>deleteCourse(@PathVariable Long id){
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(MessageConstant.COURSE_WITH_ID +id+MessageConstant.DELETED+MessageConstant.SUCCESSFULLY);
        }catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageConstant.AN_UNEXPECTED_ERROR_OCCURRED);
        }
    }
}
