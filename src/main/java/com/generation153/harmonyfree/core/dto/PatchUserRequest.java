package com.generation153.harmonyfree.core.dto;

public class PatchUserRequest {

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String displayName;
	private String profileImageUrl;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {
		return "PatchUserRequest [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", displayName=" + displayName + ", profileImageUrl=" + profileImageUrl + "]";
	}

}
