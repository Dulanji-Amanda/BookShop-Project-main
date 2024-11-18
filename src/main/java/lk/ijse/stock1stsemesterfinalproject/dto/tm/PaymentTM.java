package lk.ijse.stock1stsemesterfinalproject.dto.tm;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class PaymentTM {

    private String paymentId;
    private double amount;
    private int contact;
    private LocalDate paymentDate;
    private String orderId;
}
