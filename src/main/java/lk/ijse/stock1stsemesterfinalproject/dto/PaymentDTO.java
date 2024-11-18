package lk.ijse.stock1stsemesterfinalproject.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class PaymentDTO {
    private String paymentId;
    private double amount;
    private int contact;
    private LocalDate paymentDate;
    private String orderId;
}
