package edutech.backend.service;

import edutech.backend.entity.CourseModule;
import edutech.backend.exception.CourseModuleNotFoundException;
import edutech.backend.repository.CourseModuleRepository;
import edutech.backend.util.MessageConstant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseModuleServiceImpl implements CourseModuleService {

    private static final Logger logger = LoggerFactory.getLogger(CourseModuleServiceImpl.class);
    private final CourseModuleRepository courseModuleRepository;

    @Override
    public List<CourseModule> getAllCourseModules() {
        logger.info("Fetching all course modules.");
        List<CourseModule> courseModules = courseModuleRepository.findAll();
        logger.info("Successfully fetched {} course modules.", courseModules.size());
        return courseModules;
    }

    @Override
    public CourseModule getCourseModuleById(Long id) throws CourseModuleNotFoundException {
        logger.info("Fetching course module with ID: {}", id);
        CourseModule courseModule = courseModuleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Course module with ID: {} not found.", id);
                    return new CourseModuleNotFoundException(MessageConstant.COURSE_MODULE_NOT_FOUND + id);
                });
        logger.info("Successfully fetched course module with ID: {}", id);
        return courseModule;
    }

    @Override
    public CourseModule createCourseModule(CourseModule courseModule) {
        logger.info("Creating a new course module: {}", courseModule);
        CourseModule savedCourseModule = courseModuleRepository.save(courseModule);
        logger.info("Successfully created course module with ID: {}", savedCourseModule.getId());
        return savedCourseModule;
    }

    @Override
    public CourseModule updateCourseModule(Long id, CourseModule updatedCourseModule) throws CourseModuleNotFoundException {
        logger.info("Updating course module with ID: {}", id);
        if (!courseModuleRepository.existsById(id)) {
            logger.error("Course module with ID: {} not found for update.", id);
            throw new CourseModuleNotFoundException(MessageConstant.COURSE_MODULE_NOT_FOUND + id);
        }
        updatedCourseModule.setId(id);  // Ensure the ID is set on the updated entity
        CourseModule savedCourseModule = courseModuleRepository.save(updatedCourseModule);
        logger.info("Successfully updated course module with ID: {}", savedCourseModule.getId());
        return savedCourseModule;
    }

    @Override
    public void deleteCourseModuleById(Long id) throws CourseModuleNotFoundException {
        logger.info("Deleting course module with ID: {}", id);
        if (!courseModuleRepository.existsById(id)) {
            logger.error("Course module with ID: {} not found for deletion.", id);
            throw new CourseModuleNotFoundException(MessageConstant.COURSE_MODULE_NOT_FOUND + id);
        }
        courseModuleRepository.deleteById(id);
        logger.info("Successfully deleted course module with ID: {}", id);
    }
}
