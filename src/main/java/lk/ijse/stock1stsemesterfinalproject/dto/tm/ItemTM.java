package lk.ijse.stock1stsemesterfinalproject.dto.tm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class ItemTM {
    private String Item_Id;
    private String Item_Name;
    private int Qty;
    private Double Price;
}
