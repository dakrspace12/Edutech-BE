package edutech.backend.service;

import edutech.backend.entity.Course;
import edutech.backend.exception.CourseNotFoundException;
import edutech.backend.repository.CourseRepository;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Course findCourseById(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        return courseOptional.orElseThrow(() -> new CourseNotFoundException(MessageConstant.COURSE_WITH_ID+ id+MessageConstant.NOT_FOUND));
    }

    @Transactional
    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    @Override
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(MessageConstant.COURSE_WITH_ID+ id+MessageConstant.NOT_FOUND));

        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());

        return courseRepository.save(existingCourse);
    }

    @Transactional
    @Override
    public void deleteCourseById(Long id) {
        if (!courseRepository.existsById(id)){
            throw new CourseNotFoundException(MessageConstant.COURSE_WITH_ID+id+MessageConstant.NOT_FOUND);
        }
        courseRepository.deleteById(id);
    }
}