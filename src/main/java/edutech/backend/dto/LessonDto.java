package edutech.backend.dto;

import java.util.Date;

public class LessonDto {
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Long courseModuleId;  // This property should be present to send the CourseModule ID

    public LessonDto(Long id, String title, String content, Date createdAt, Long courseModuleId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.courseModuleId = courseModuleId;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCourseModuleId() {
        return courseModuleId;
    }

    public void setCourseModuleId(Long courseModuleId) {
        this.courseModuleId = courseModuleId;
    }
}
