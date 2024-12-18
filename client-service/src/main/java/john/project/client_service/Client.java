package john.project.client_service;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;
    
    @Column(name = "client_first_name", nullable = false)
    private String name;
    @Column(name = "client_second_name", nullable = false)
    private String surname;
    @Column(name = "client_patronymic")
    private String patronymic;
    @Column(name = "client_phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "client_email", nullable = false, unique = true)
    private String email;
    @Column(name = "client_password", nullable = false)
    private String pass;
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    @PrePersist
    public void setDefaultRoleId() {
        if (roleId == null) {
            roleId = (long)2;
        }
    }

    public Client() {}

    public Client(String name, String surname, String patronymic, String phoneNumber, String email, String pass, Long roleId) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pass = pass;
        this.roleId = roleId;
    }

    public Long getId() {
        return client_id;
    }

    public void setId(Long client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public Long getRole() {
        return roleId;
    }

    public void setRole(Long roleId) {
        this.roleId = roleId;
    }
}
