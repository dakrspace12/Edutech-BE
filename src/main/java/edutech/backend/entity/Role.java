package edutech.backend.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor  // Lombok generates a no-args constructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private RoleName name;  // Ensures roles are stored as strings in the DB

	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	// Enum for role types
	public enum RoleName {
		ROLE_USER,
		ROLE_ADMIN
	}
	// Constructor with RoleName
	public Role(RoleName name) {
		this.name = name;
	}
}
