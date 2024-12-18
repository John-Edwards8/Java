package john.project.frontend_service;

public class OrderMemento {
    private final OrderResponseDto orderState;

    public OrderMemento(OrderResponseDto orderState) {
        this.orderState = new OrderResponseDto(orderState);
    }

    public OrderResponseDto getState() {
        return orderState;
    }
}
