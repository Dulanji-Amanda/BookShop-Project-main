package lk.ijse.stock1stsemesterfinalproject.dto.tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class OrderTM {
    private String Order_Id;
    private String Item_Id;
    private String Payment_Id;
    private String Customer_Id;
    private int Qty;
    private LocalDate date;
    private double totalAmount;
}
