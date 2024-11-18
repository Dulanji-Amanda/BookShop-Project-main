package lk.ijse.stock1stsemesterfinalproject.dto.tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SupplierOrderDetailTM {
    private String Stock_Id;
    private String Sup_Id;
    private LocalDate Date;
}
