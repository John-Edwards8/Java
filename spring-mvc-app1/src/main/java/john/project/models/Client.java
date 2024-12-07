package john.project.models;

import jakarta.validation.constraints.*;

public class Client {
    @NotEmpty(message = "Id should not be empty")
	private final int id;
	
    @NotBlank(message = "Name should not be empty")
    private final String name;

    @NotBlank(message = "Surname should not be empty")
    private final String surname;

    private final String patronymic;

    @Pattern(regexp = "\\d{10}", message = "Phone number should include 10 numbers")
    private final String phoneNumber;

    @Email(message = "Incorrect format")
    private final String email;
    
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
    
	Client(ClientBuilder builder) {
		this.id=builder.id;
		this.name=builder.name;
		this.surname=builder.surname;
		this.patronymic=builder.patronymic;
		this.phoneNumber=builder.phoneNumber;
		this.email=builder.email;
	}
	
	public static class ClientBuilder{
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

		public ClientBuilder id(int id2) {
			this.id=id2;
			return this;
		}
		public ClientBuilder name(String name2) {
			this.name=name2;
			return this;
		}
		public ClientBuilder surname(String surname2) {
			this.surname=surname2;
			return this;
		}
		public ClientBuilder patronymic(String patronymic2) {
			this.patronymic=patronymic2;
			return this;
		}
		public ClientBuilder phoneNumber(String phoneNumber2) {
			this.phoneNumber=phoneNumber2;
			return this;
		}
		public ClientBuilder email(String email2) {
			this.email=email2;
			return this;
		}
		
		public Client build(){
			return new Client(this);
		}

	}

}