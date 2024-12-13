package john.project.dao;

public interface Observer {
	void update(String eventType, Object data);
}
