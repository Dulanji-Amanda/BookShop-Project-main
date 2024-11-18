package lk.ijse.stock1stsemesterfinalproject.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class ItemDTO {
    private String Item_Id;
    private String Item_Name;
    private Integer Qty;
    private Double Price;
}
