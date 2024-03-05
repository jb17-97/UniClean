package com.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long userId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "post_code", nullable = false)
	private String postCode;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "telephone", nullable = false)
	private String telephone;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "role", nullable = false)
	private String role;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "status")
	private String status;
}
