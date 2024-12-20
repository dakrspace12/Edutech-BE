package edutech.backend.repository;
import edutech.backend.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<CourseModule, Long> {
}
