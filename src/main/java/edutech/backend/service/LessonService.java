package edutech.backend.service;

import edutech.backend.dto.LessonDto;
import java.util.List;

public interface LessonService {
    LessonDto getLessonById(Long id);
    List<LessonDto> getAllLessons();
    LessonDto createLesson(LessonDto lessonDto);
}
