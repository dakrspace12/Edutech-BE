package edutech.backend.service;

import edutech.backend.entity.Course;
import edutech.backend.exception.CourseNotFoundException;
import edutech.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Get all courses from the database
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get a course by ID
    @Transactional(readOnly = true)
    public Course findCourseById(Long id) {
        // Fetch the course from the repository
        Optional<Course> courseOptional = courseRepository.findById(id);
        // If the course is present, return it. Otherwise, throw an exception
        return courseOptional.orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    // Create a new course in the database
    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // Update an existing course by ID
    @Transactional
    public Course updateCourse(Long id, Course updatedCourse) {
        // Fetch the existing course from the database
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        // Update course fields with the new data
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());

        // Save the updated course back into the database
        return courseRepository.save(existingCourse);
    }

    // Delete a course by its ID
    @Transactional
    public void deleteCourseById(Long id) throws CourseNotFoundException{
        // Try to delete the course by its ID
        if (!courseRepository.existsById(id)){
            throw new  CourseNotFoundException("Course with ID "+id+" does not exist" );
        }
            courseRepository.deleteById(id);
    }
}
