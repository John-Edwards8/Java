package john.project.dao;

import org.springframework.stereotype.Component;

@Component
public class GUIObserver implements Observer {

	@Override
	public void update(String eventType, Object data) {
        String message;
        switch (eventType) {
            case "CLIENT_ADDED":
                message = "Client added: " + data.toString();
                break;
            case "CLIENT_UPDATED":
                message = "Client updated: " + data.toString();
                break;
            case "CLIENT_DELETED":
                message = "Client deleted with ID: " + data;
                break;
            default:
                message = "Unknown event: " + eventType;
        }

        System.out.println("GUI Observer: " + message);

	}

}
