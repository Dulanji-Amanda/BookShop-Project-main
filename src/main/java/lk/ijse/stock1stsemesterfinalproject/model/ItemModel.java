package lk.ijse.stock1stsemesterfinalproject.model;


import lk.ijse.stock1stsemesterfinalproject.dto.CustomerDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.ItemDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {
    public String getNextItemId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select item_Id from item order by item_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("I%03d", newIdIndex);
        }
        return "I001";
    }

    public boolean saveItem(ItemDTO itemDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into item values (?,?,?,?)",
                itemDTO.getItem_Id(),
                itemDTO.getItem_Name(),
                itemDTO.getQty(),
                itemDTO.getPrice()
        );
    }

    public ArrayList<ItemDTO> getAllItems() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from item");

        ArrayList<ItemDTO> itemDTOS = new ArrayList<>();

        while (rst.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4)
            );
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }

    public boolean updateItem(ItemDTO itemDTO) throws SQLException {
        return CrudUtil.execute(
                "update item set Item_Name=?, Qty=?, Price=? where Item_Id=?",
                itemDTO.getItem_Name(),
                itemDTO.getQty(),
                itemDTO.getPrice(),
                itemDTO.getItem_Id()
        );
    }

    public boolean deleteItem(String item_id) throws SQLException {
        return CrudUtil.execute("delete from item where Item_Id=?", item_id);
    }

    public ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Item_Id from item");

        ArrayList<String> item_ids = new ArrayList<>();

        while (rst.next()) {
            item_ids.add(rst.getString(1));
        }

        return item_ids;
    }

    public ItemDTO findById(String selectedItemId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from item where Item_Id=?", selectedItemId);

        if (rst.next()) {
            return new ItemDTO(
                    rst.getString(1),  // customer ID
                    rst.getString(2),  // Name
                    rst.getInt(3),  // Contact
                    rst.getDouble(4)  // Contact
            );
        }
        return null;
    }

    public void updateItemMinus(String itemId, int qty) throws SQLException {
        CrudUtil.execute("UPDATE item SET Qty = Qty - ? WHERE Item_Id = ?", qty, itemId);
    }
}
