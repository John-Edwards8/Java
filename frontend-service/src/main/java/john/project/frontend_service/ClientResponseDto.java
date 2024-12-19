package john.project.frontend_service;

public class ClientResponseDto {
	private Long id;
	private String name;

    private String surname;

    private String patronymic;

    private String phoneNumber;

    private String email;
    
    private String pass;
    
    private Long role;
    
	public ClientResponseDto(Object invalid, String name, String surname, String patronymic, String phoneNumber, String email,
			String pass, long l) {
		this.id = (Long) invalid;
    	this.name = name;
    	this.surname = surname;
    	this.patronymic = patronymic;
    	this.phoneNumber = phoneNumber;
    	this.email = email;
    	this.pass = pass;
    	this.role = l;
    }

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
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

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}



}
