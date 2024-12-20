package edutech.backend.service;

import edutech.backend.entity.CourseModule;
import edutech.backend.exception.CourseModuleNotFoundException;

import java.util.List;

public interface CourseModuleService {
    List<CourseModule> getAllCourseModules();
    CourseModule getCourseModuleById(Long id) throws CourseModuleNotFoundException;
    CourseModule createCourseModule(CourseModule courseModule);
    CourseModule updateCourseModule(Long id, CourseModule updatedCourseModule) throws CourseModuleNotFoundException;
    void deleteCourseModuleById(Long id) throws CourseModuleNotFoundException;
}