package edutech.backend.controller;



import edutech.backend.dto.ApiResponse;
import edutech.backend.dto.LoginRequest;
import edutech.backend.dto.SignupRequest;
import edutech.backend.entity.Course;
import edutech.backend.entity.User;
import edutech.backend.service.AdminService;
import edutech.backend.service.AuthService;
import edutech.backend.service.CourseService;
import edutech.backend.service.UserServiceImpl;
import edutech.backend.util.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired private AuthService authService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.USER_DELETED_SUCCESSFULLY, null));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        String token = authService.registerUser(signupRequest);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.USER_REGISTERED_SUCCESSFULLY, token));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.USER_AUTHENTICATED_SUCCESSFULLY, token));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse> createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.COURSE_CREATED_SUCCESSFULLY, null));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse> deleteCourseById(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.COURSE_DELETED_SUCCESSFULLY, null));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        courseService.updateCourse(id, course);
        return ResponseEntity.ok(new ApiResponse(true, MessageConstant.COURSE_UPDATED_SUCCESSFULLY, null));
    }
}
