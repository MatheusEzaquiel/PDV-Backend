package com.mbe.viapdv.model.userRole;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.user.User;


@Embeddable
public class UserRoleId implements Serializable {

	private static final long serialVersionUID = 318309625569290001L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;
	

	public UserRoleId() {}

	public UserRoleId(User user, Role role) {
		this.user = user;
		this.role = role;
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserRoleId that = (UserRoleId) o;
		return Objects.equals(user, that.user) && Objects.equals(role, that.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, role);
	}
}
