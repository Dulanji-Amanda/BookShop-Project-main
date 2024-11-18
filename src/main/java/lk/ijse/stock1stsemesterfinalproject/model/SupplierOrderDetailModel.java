package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.SupplierOrderDetailDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SupplierOrderDetailModel {

    public String getNextOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Stock_Id from supplier_stock_detail order by Stock_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last Stock ID
            String substring = lastId.substring(1); // Extract numeric part
            int i = Integer.parseInt(substring); // Convert to integer
            int newIdIndex = i + 1; // Increment number by 1
            return String.format("T%03d", newIdIndex); // Return new ID in format Snnn
        }
        return "S001"; // Default ID if no data found
    }

    public boolean saveOrderDetail(SupplierOrderDetailDTO orderDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into supplier_stock_detail (Stock_Id, Sup_Id, Date) values (?, ?, ?)",
                orderDetailDTO.getStock_Id(),
                orderDetailDTO.getSup_Id(),
                orderDetailDTO.getDate()
        );
    }

    public ArrayList<SupplierOrderDetailDTO> getAllOrderDetails() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier_stock_detail");

        ArrayList<SupplierOrderDetailDTO> orderDetails = new ArrayList<>();

        while (rst.next()) {
            SupplierOrderDetailDTO orderDetailDTO = new SupplierOrderDetailDTO(
                    rst.getDate(1).toLocalDate(), // Date
                    rst.getString(2),  // Stock ID
                    rst.getString(3)  // Supplier ID
            );
            orderDetails.add(orderDetailDTO);
        }
        return orderDetails;
    }

    public boolean updateOrderDetail(SupplierOrderDetailDTO orderDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "update supplier_stock_detail set Sup_Id=?, Date=? where Stock_Id=?",
                orderDetailDTO.getSup_Id(),
                orderDetailDTO.getDate(),
                orderDetailDTO.getStock_Id()
        );
    }

    public boolean deleteOrderDetail(String stockId) throws SQLException {
        return CrudUtil.execute("delete from supplier_stock_detail where Stock_Id=?", stockId);
    }

    public ArrayList<String> getAllStockIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Stock_Id from supplier_stock_detail");

        ArrayList<String> stockIds = new ArrayList<>();

        while (rst.next()) {
            stockIds.add(rst.getString(1));
        }

        return stockIds;
    }

    public SupplierOrderDetailDTO findById(String stockId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier_stock_detail where Stock_Id=?", stockId);

        if (rst.next()) {
            return new SupplierOrderDetailDTO(
                    rst.getDate(1).toLocalDate(), // Date
                    rst.getString(2),  // Stock ID
                    rst.getString(3) // Supplier ID
            );
        }
        return null;
    }
}
