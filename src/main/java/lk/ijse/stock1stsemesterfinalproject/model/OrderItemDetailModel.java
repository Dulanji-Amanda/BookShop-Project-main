package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.OrderDetailDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderItemDetailModel {

    public boolean saveOrderItemDetail(String orderId, String itemId, LocalDate date, double amount) throws SQLException {
        return CrudUtil.execute("INSERT INTO order_item_detail (Order_Id, Item_Id, Date, Amount) VALUES (?,?,?,?)",
                orderId, itemId, date, amount);
    }

    public OrderDetailDTO findById(String orderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM order_item_detail WHERE Order_Id=?", orderId);
        if (rst.next()) {
            return new OrderDetailDTO(
                    rst.getString("Order_Id"),
                    rst.getString("Item_Id"),
                    rst.getDate("Date").toLocalDate(),
                    rst.getDouble("Amount")
            );
        }
        return null;
    }

    public ArrayList<OrderDetailDTO> findAllByOrderId(String orderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM order_item_detail WHERE Order_Id=?", orderId);
        ArrayList<OrderDetailDTO> details = new ArrayList<>();
        while (rst.next()) {
            details.add(new OrderDetailDTO(
                    rst.getString("Order_Id"),
                    rst.getString("Item_Id"),
                    rst.getDate("Date").toLocalDate(),
                    rst.getDouble("Amount")
            ));
        }
        return details;
    }
}
