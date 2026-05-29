package com.generation153.harmonyfree.core.dto;

public class CreateUserRequest {

	private String username;
	private String firstName;
	private String lastName;
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

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {
		return "CreateUserRequest [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", profileImageUrl=" + profileImageUrl + "]";
	}
	
}
