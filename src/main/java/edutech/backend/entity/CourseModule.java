package edutech.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String moduleName;
    private String moduleDescription;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}

