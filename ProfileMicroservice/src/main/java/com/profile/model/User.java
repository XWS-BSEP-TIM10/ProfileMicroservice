package com.profile.model;

import com.profile.dto.NewUserDto;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "biography")
    private String biography;

    public User(String firstName, String lastName, String email, String phoneNumber, String gender, String dateOfBirth, String username, String biography) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.biography = biography;
    }

    public User(NewUserDto dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.phoneNumber = dto.getPhoneNumber();
        this.gender = dto.getGender();
        this.dateOfBirth = dto.getDateOfBirth();
        this.username = dto.getUsername();
        this.biography = dto.getBiography();
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getBiography() {
        return biography;
    }
}
