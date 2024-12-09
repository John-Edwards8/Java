package john.project.models;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;

public class OrderForm implements Cloneable {
    @NotEmpty(message = "Id should not be empty")
	private int id;

    @NotEmpty(message = "Date should not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @NotEmpty(message = "Status should not be empty")
    private String status;
    
    public OrderForm() {}

    public OrderForm(OrderForm original) {
        this.date = original.date;
        this.status = original.status;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
