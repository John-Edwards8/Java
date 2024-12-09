package john.project.models;

import java.util.Stack;

import org.springframework.stereotype.Component;

@Component
public class OrderHistory {
    private final Stack<OrderMemento> history = new Stack<>();

    public void saveState(OrderForm order) {
        history.push(new OrderMemento(order));
    }

    public OrderForm restoreState() {
        if (!history.isEmpty()) {
            return history.pop().getState();
        }
        return null;
    }
}
