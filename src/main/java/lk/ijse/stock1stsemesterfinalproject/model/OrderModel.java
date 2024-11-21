package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.OrderDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {
    public String getNextOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Order_Id from orders order by Order_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("O%03d", newIdIndex);
        }
        return "O001";
    }

    public boolean saveOrder(OrderDTO orderDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into orders (Order_Id, Description, Order_qty, Cust_Id) values (?,?,?,?)",
                orderDTO.getOrder_Id(),
                orderDTO.getDescription(),
                orderDTO.getOrder_qty(),
                orderDTO.getCust_Id()
        );
    }

    // Method to get all orders
    public ArrayList<OrderDTO> getAllOrders() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from orders");

        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();

        while (rst.next()) {
            OrderDTO orderDTO = new OrderDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            );
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    public boolean updateOrder(OrderDTO orderDTO) throws SQLException {
        return CrudUtil.execute(
                "update orders set Description=?, Cust_Id=? where Order_Id=?",
                orderDTO.getDescription(),
                orderDTO.getCust_Id(),
                orderDTO.getOrder_Id()
        );
    }

    public boolean deleteOrder(String order_id) throws SQLException {
        return CrudUtil.execute("delete from orders where Order_Id=?", order_id);
    }

    public ArrayList<String> getAllOrderIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Order_Id from orders");

        ArrayList<String> order_ids = new ArrayList<>();

        while (rst.next()) {
            order_ids.add(rst.getString(1));
        }

        return order_ids;
    }

    public OrderDTO findById(String orderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from orders where Order_Id=?", orderId);

        if (rst.next()) {
            return new OrderDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            );
        }

        return null;
    }
}
