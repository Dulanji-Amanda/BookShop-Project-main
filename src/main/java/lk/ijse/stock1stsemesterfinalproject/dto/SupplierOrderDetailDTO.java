package lk.ijse.stock1stsemesterfinalproject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SupplierOrderDetailDTO {
    private LocalDate Date;
    private String Stock_Id;
    private String Sup_Id;
}
