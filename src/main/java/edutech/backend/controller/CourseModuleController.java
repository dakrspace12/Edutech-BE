package edutech.backend.controller;

import edutech.backend.entity.Course;
import edutech.backend.entity.CourseModule;
import edutech.backend.exception.CourseModuleNotFoundException;
import edutech.backend.exception.CourseNotFoundException;
import edutech.backend.service.CourseModuleService;
import edutech.backend.repository.CourseRepository;  // Import CourseRepository
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-modules")
@RequiredArgsConstructor
public class CourseModuleController {

    private static final Logger logger = LoggerFactory.getLogger(CourseModuleController.class);
    private final CourseModuleService courseModuleService;
    private final CourseRepository courseRepository;


    @GetMapping("/{id}")
    public ResponseEntity<CourseModule> getCourseModuleById(@PathVariable Long id) {
        try {
            CourseModule courseModule = courseModuleService.getCourseModuleById(id);
            logger.info("Course Module with ID: {} fetched successfully", id);
            return ResponseEntity.ok(courseModule);
        } catch (CourseModuleNotFoundException e) {
            logger.error("Course Module not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping
    public ResponseEntity<List<CourseModule>> getAllCourseModules() {
        logger.info("Fetching all course modules");
        List<CourseModule> courseModules = courseModuleService.getAllCourseModules();
        logger.info("Total Course Modules fetched: {}", courseModules.size());
        return ResponseEntity.ok(courseModules);
    }


    @PostMapping
    public ResponseEntity<CourseModule> createCourseModule(@RequestBody CourseModule courseModule) {
        try {

            Course course = courseRepository.findById(courseModule.getCourse().getId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID " + courseModule.getCourse().getId()));


            courseModule.setCourse(course);
            CourseModule createdModule = courseModuleService.createCourseModule(courseModule);
            logger.info("Course Module with ID: {} created successfully", createdModule.getId());
            return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
        } catch (CourseNotFoundException e) {
            logger.error("Error creating Course Module: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourseModule> updateCourseModule(@PathVariable Long id, @RequestBody CourseModule updatedCourseModule) {
        try {
            CourseModule updatedModule = courseModuleService.updateCourseModule(id, updatedCourseModule);
            logger.info("Course Module with ID: {} updated successfully", updatedModule.getId());
            return ResponseEntity.ok(updatedModule);
        } catch (CourseModuleNotFoundException e) {
            logger.error("Course Module not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseModule(@PathVariable Long id) {
        try {
            courseModuleService.deleteCourseModuleById(id);
            logger.info("Course Module with ID: {} deleted successfully", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course Module deleted successfully");
        } catch (CourseModuleNotFoundException e) {
            logger.error("Course Module not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
