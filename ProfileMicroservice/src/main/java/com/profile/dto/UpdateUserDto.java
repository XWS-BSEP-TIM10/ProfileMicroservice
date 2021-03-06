package com.profile.dto;

import com.profile.model.User;

public class UpdateUserDto {

    private String uuid;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String gender;

    private String dateOfBirth;

    private String username;

    private String biography;

    private boolean publicProfile;

    private boolean muteConnectionsNotifications;

    private boolean muteMassageNotifications;

    private boolean mutePostNotifications;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String uuid, String firstName, String lastName, String email, String phoneNumber, String gender, String dateOfBirth, String username, String biography, boolean publicProfile, boolean muteConnectionsNotifications, boolean muteMassageNotifications, boolean mutePostNotifications) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.biography = biography;
        this.publicProfile = publicProfile;
        this.muteConnectionsNotifications = muteConnectionsNotifications;
        this.muteMassageNotifications = muteMassageNotifications;
        this.mutePostNotifications = mutePostNotifications;
    }

    public UpdateUserDto(User user) {
        this.uuid = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.gender = user.getGender().toString();
        this.username = user.getUsername();
        this.biography = user.getBiography();
        this.muteConnectionsNotifications = user.isMuteConnectionsNotifications();
        this.muteMassageNotifications = user.isMuteMassageNotifications();
        this.mutePostNotifications = user.isMutePostNotifications();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    public boolean isMuteConnectionsNotifications() {
        return muteConnectionsNotifications;
    }

    public void setMuteConnectionsNotifications(boolean muteConnectionsNotifications) {
        this.muteConnectionsNotifications = muteConnectionsNotifications;
    }

    public boolean isMuteMassageNotifications() {
        return muteMassageNotifications;
    }

    public void setMuteMassageNotifications(boolean muteMassageNotifications) {
        this.muteMassageNotifications = muteMassageNotifications;
    }

    public boolean isMutePostNotifications() {
        return mutePostNotifications;
    }

    public void setMutePostNotifications(boolean mutePostNotifications) {
        this.mutePostNotifications = mutePostNotifications;
    }

    @Override
    public String toString() {
        return "NewUserDTO{" +
                "uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", username='" + username + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
