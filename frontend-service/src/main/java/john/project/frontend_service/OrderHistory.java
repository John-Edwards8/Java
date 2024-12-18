package john.project.frontend_service;

import java.util.Stack;

import org.springframework.stereotype.Component;

@Component
public class OrderHistory {
    private final Stack<OrderMemento> history = new Stack<>();

    public void saveState(OrderResponseDto order) {
        history.push(new OrderMemento(order));
    }

    public OrderResponseDto restoreState() {
        if (!history.isEmpty()) {
            return history.pop().getState();
        }
        return null;
    }
}
