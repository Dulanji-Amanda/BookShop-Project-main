package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.StockDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockModel {
    public String getNextStockId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Stock_Id from stock order by Stock_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last stock ID
            String substring = lastId.substring(1); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("T%03d", newIdIndex); // Return the new stock ID in format Cnnn
        }
        return "T001"; // Return the default customer ID if no data is found
    }

    public boolean saveStock(StockDTO stockDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into stock values (?,?,?)",
                stockDTO.getStock_Id(),
                stockDTO.getName(),
                stockDTO.getUser_Id()
        );
    }

    public ArrayList<StockDTO> getAllStocks() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from stock");

        ArrayList<StockDTO> stockDTOS = new ArrayList<>();

        while (rst.next()) {
            StockDTO stockDTO = new StockDTO(
                    rst.getString(1),  // stock ID
                    rst.getString(2),  // name ID
                    rst.getString(3) // user id

            );
            stockDTOS.add(stockDTO);
        }
        return stockDTOS;
    }

    public boolean updateStock(StockDTO stockDTO) throws SQLException {
        return CrudUtil.execute(
                "update stock set Name=?,User_Id=? where Stock_Id=?",
                stockDTO.getName(),
                stockDTO.getUser_Id(),
                stockDTO.getStock_Id()
        );
    }

    public boolean deleteStock(String Stock_Id) throws SQLException {
        return CrudUtil.execute("delete from stock where Stock_Id=?", Stock_Id);
    }

    public ArrayList<String> getAllStockIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Stock_Id from stock");

        ArrayList<String> Stock_Ids = new ArrayList<>();

        while (rst.next()) {
            Stock_Ids.add(rst.getString(1));
        }

        return Stock_Ids;
    }

    public StockDTO findById(String selectedCusId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from stock where Stock_Id=?", selectedCusId);

        if (rst.next()) {
            return new StockDTO(
                    rst.getString(1),  // stock ID
                    rst.getString(2), // name
                    rst.getString(3) // user id
            );
        }

        return null;
    }
}
