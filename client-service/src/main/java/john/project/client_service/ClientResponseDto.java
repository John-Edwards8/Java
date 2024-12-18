package john.project.client_service;

public class ClientResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String pass;
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long long1) {
        this.id = long1;
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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
