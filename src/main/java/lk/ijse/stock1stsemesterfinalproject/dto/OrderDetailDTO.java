package lk.ijse.stock1stsemesterfinalproject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class OrderDetailDTO {
    private String orderId;
    private String itemId;
    private LocalDate date;
    private double amount;
}
