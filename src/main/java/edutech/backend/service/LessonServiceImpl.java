package edutech.backend.service;

import edutech.backend.dto.LessonDto;
import edutech.backend.entity.Lesson;
import edutech.backend.entity.CourseModule;
import edutech.backend.repository.LessonRepository;
import edutech.backend.repository.CourseModuleRepository;
import edutech.backend.util.MessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseModuleRepository courseModuleRepository;  // Correctly declare the repository for CourseModule
    private static final Logger logger = LoggerFactory.getLogger(LessonServiceImpl.class);

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, CourseModuleRepository courseModuleRepository) {
        this.lessonRepository = lessonRepository;
        this.courseModuleRepository = courseModuleRepository;  // Ensure the CourseModuleRepository is injected
    }

    @Override
    public LessonDto getLessonById(Long id) {
        logger.info("Fetching lesson with ID: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstant.LESSON_NOT_FOUND));
        logger.info("Lesson with ID: {} fetched successfully", id);


        return new LessonDto(lesson.getId(), lesson.getTitle(),
                lesson.getContent(), lesson.getCreatedAt(),
                lesson.getCourseModule().getId());
    }

    @Override
    public List<LessonDto> getAllLessons() {
        logger.info("Fetching all lessons");
        List<Lesson> lessons = lessonRepository.findAll();
        logger.info("Total lessons fetched: {}", lessons.size());


        return lessons.stream().map(lesson -> new LessonDto(lesson.getId(), lesson.getTitle(),
                        lesson.getContent(), lesson.getCreatedAt(),
                        lesson.getCourseModule().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {

        if (lessonDto.getCourseModuleId() == null) {
            throw new RuntimeException("CourseModule ID must not be null");
        }

        logger.info("Creating new lesson with title: {}", lessonDto.getTitle());
        logger.info("CourseModule ID passed: {}", lessonDto.getCourseModuleId());


        CourseModule courseModule = courseModuleRepository.findById(lessonDto.getCourseModuleId())
                .orElseThrow(() -> {
                    logger.error("CourseModule with ID {} not found", lessonDto.getCourseModuleId());
                    return new RuntimeException("CourseModule not found");
                });


        logger.info("Fetched CourseModule: {}", courseModule);


        Lesson lesson = new Lesson(null, lessonDto.getTitle(),
                lessonDto.getContent(), new Date(), courseModule);


        lesson = lessonRepository.save(lesson);

        logger.info("Lesson with ID: {} created successfully", lesson.getId());

        return new LessonDto(lesson.getId(), lesson.getTitle(),
                lesson.getContent(), lesson.getCreatedAt(),
                lesson.getCourseModule().getId());
    }
}
