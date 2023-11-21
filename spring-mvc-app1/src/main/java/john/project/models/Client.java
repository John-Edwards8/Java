package john.project.models;
import jakarta.validation.constraints.*;


public class Client {
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
    
    public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setId(int newId) {
		id = newId;
	}
	public void setName(String string) {
		name = string;	
	}
	public void setSurname(String string) {
		surname = string;		
	}
	public void setPatronymic(String string) {
		patronymic = string;		
	}
	public void setPhoneNumber(String string) {
		phoneNumber = string;		
	}
	public void setEmail(String string) {
		email = string;		
	}

}