package john.project.models;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;

public class Order {
    @NotEmpty(message = "Id should not be empty")
	private final int id;

    @NotEmpty(message = "Date should not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final Date date;

    @NotEmpty(message = "Status should not be empty")
    private final String status;

    Order(OrderBuilder builder) {
		this.id=builder.id;
		this.date=builder.date;
		this.status=builder.status;
	}
	public int getId() {
		return id;
	}
	public Date getDate() {
	    return date;
    }
    public String getStatus() {
        return status;
    }
    	
    public static class OrderBuilder{
    	private int id;

        @NotEmpty(message = "Date should not be empty")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date date;

        @NotEmpty(message = "Status should not be empty")
        private String status;

		public OrderBuilder id(int id2) {
			this.id=id2;
			return this;
		}
	    public OrderBuilder date(Date date2) {
	        this.date = date2;
	        return this;
	    }
	    public OrderBuilder status(String status2) {
	        this.status = status2;
	        return this;
	    }
		
		public Order build(){
			return new Order(this);
		}

	}
    
}
