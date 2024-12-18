package john.project.client_service;

public class ClientMapper {
    public static Client toEntity(ClientCreateDto dto) {
        return new Client(
            dto.getName(),
            dto.getSurname(),
            dto.getPatronymic(),
            dto.getPhoneNumber(),
            dto.getEmail(),
            dto.getPass(),
            dto.getRoleId());
    }

    public static ClientResponseDto toDto(Client client) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setSurname(client.getSurname());
        dto.setPatronymic(client.getPatronymic());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setEmail(client.getEmail());
        dto.setPass(client.getPass());
        return dto;
    }
}
