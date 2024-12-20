package edutech.backend.controller;

import edutech.backend.dto.LessonDto;
import edutech.backend.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonService lessonService;

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        logger.info("Fetching lesson with ID: {}", id);
        LessonDto lesson = lessonService.getLessonById(id);
        logger.info("Lesson with ID: {} fetched successfully", id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        logger.info("Fetching all lessons");
        List<LessonDto> lessons = lessonService.getAllLessons();
        logger.info("Total lessons fetched: {}", lessons.size());
        return ResponseEntity.ok(lessons);
    }

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        logger.info("Creating new lesson with title: {}", lessonDto.getTitle());
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        logger.info("Lesson with ID: {} created successfully", createdLesson.getId());
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }
}
