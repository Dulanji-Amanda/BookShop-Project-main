package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.ItemDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.StockDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.SupplierDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class StockModel {
    public String getNextStockId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Stock_Id from stock order by Stock_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("T%03d", newIdIndex);
        }
        return "T001";
    }

    public boolean saveStock(StockDTO stockDTO, ItemDTO itemsDTO, SupplierDTO suppliersDTO, String text) throws SQLException {
        boolean transactionSuccess = false;

        try {
            CrudUtil.setAutoCommit(false);

            boolean stockSaved = CrudUtil.execute(
                    "insert into stock values (?,?,?)",
                    stockDTO.getStock_Id(),
                    stockDTO.getName(),
                    stockDTO.getUser_Id()
            );

            boolean itemUpdated = CrudUtil.execute(
                    "UPDATE item SET Qty = ? WHERE Item_Id = ?",
                    text,
                    itemsDTO.getItem_Id()
            );

            boolean stockItemSaved = CrudUtil.execute(
                    "INSERT INTO stock_item_detail (Stock_Id, Item_Id) VALUES (?, ?)",
                    stockDTO.getStock_Id(),
                    itemsDTO.getItem_Id()
            );

            boolean supplierStockSaved = CrudUtil.execute(
                    "INSERT INTO supplier_stock_detail (Date, Stock_Id, Sup_Id) VALUES (?, ?, ?)",
                    LocalDate.now(),
                    stockDTO.getStock_Id(),
                    suppliersDTO.getSup_Id()
            );

            if (stockSaved && itemUpdated && stockItemSaved && supplierStockSaved) {
                CrudUtil.commit();
                transactionSuccess = true;
            } else {
                CrudUtil.rollback();
            }
        } catch (SQLException e) {
            CrudUtil.rollback();
            e.printStackTrace();
        } finally {
            CrudUtil.setAutoCommit(true);
        }
        return transactionSuccess;
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

    public boolean updateStock(StockDTO stockDTO, ItemDTO itemsDTO, SupplierDTO suppliersDTO) throws SQLException {
        boolean transactionSuccess = false;

        try {
            CrudUtil.setAutoCommit(false);

            boolean stockUpdated = CrudUtil.execute(
                    "update stock set Name=?, User_Id=? where Stock_Id=?",
                    stockDTO.getName(),
                    stockDTO.getUser_Id(),
                    stockDTO.getStock_Id()
            );

            boolean itemUpdated = CrudUtil.execute(
                    "UPDATE item SET Qty = ? WHERE Item_Id = ?",
                    itemsDTO.getQty(),
                    itemsDTO.getItem_Id()
            );

            boolean stockItemUpdated = CrudUtil.execute(
                    "UPDATE stock_item_detail SET Item_Id = ? WHERE Stock_Id = ?",
                    itemsDTO.getItem_Id(),
                    stockDTO.getStock_Id()
            );

            boolean supplierStockUpdated = CrudUtil.execute(
                    "UPDATE supplier_stock_detail SET Date = ?, Sup_Id = ? WHERE Stock_Id = ?",
                    LocalDate.now(),
                    suppliersDTO.getSup_Id(),
                    stockDTO.getStock_Id()
            );

            if (stockUpdated && itemUpdated && stockItemUpdated && supplierStockUpdated) {
                CrudUtil.commit();
                transactionSuccess = true;
            } else {
                CrudUtil.rollback();
            }
        } catch (SQLException e) {
            CrudUtil.rollback();
            e.printStackTrace();
        } finally {
            CrudUtil.setAutoCommit(true);
        }

        return transactionSuccess;
    }

    public boolean deleteStock(String stockId) throws SQLException {
        boolean transactionSuccess = false;

        try {
            CrudUtil.setAutoCommit(false);

            boolean stockItemDeleted = CrudUtil.execute(
                    "DELETE FROM stock_item_detail WHERE Stock_Id = ?",
                    stockId
            );

            boolean supplierStockDeleted = CrudUtil.execute(
                    "DELETE FROM supplier_stock_detail WHERE Stock_Id = ?",
                    stockId
            );

            boolean stockDeleted = CrudUtil.execute(
                    "DELETE FROM stock WHERE Stock_Id = ?",
                    stockId
            );

            if (stockItemDeleted && supplierStockDeleted && stockDeleted) {
                CrudUtil.commit();
                transactionSuccess = true;
            } else {
                CrudUtil.rollback();
            }
        } catch (SQLException e) {
            CrudUtil.rollback();
            e.printStackTrace();
        } finally {
            CrudUtil.setAutoCommit(true);
        }

        return transactionSuccess;
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
