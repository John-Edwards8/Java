package john.project.models;

public class OrderMemento {
    private final OrderForm orderState;

    public OrderMemento(OrderForm orderState) {
        this.orderState = new OrderForm(orderState);
    }

    public OrderForm getState() {
        return orderState;
    }
}
