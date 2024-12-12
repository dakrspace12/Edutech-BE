package edutech.backend.service;

import edutech.backend.entity.Course;
import java.util.List;

public interface CourseService {


    List<Course> getAllCourses();


    Course findCourseById(Long id);


    Course createCourse(Course course);

    Course updateCourse(Long id, Course updatedCourse);


    String deleteCourseById(Long id);
}
