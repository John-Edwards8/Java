package john.project.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class ClientForm {
    @NotEmpty(message = "Id should not be empty")
	private int id;
	
    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Surname should not be empty")
    private String surname;

    private String patronymic;

    @Pattern(regexp = "\\d{10}", message = "Phone number should include 10 numbers")
    private String phoneNumber;

    @Email(message = "Incorrect format")
    private String email;
    
    public int getId() { return id;	}
    public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }
	public String getPatronymic() { return patronymic; }
	public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
}
