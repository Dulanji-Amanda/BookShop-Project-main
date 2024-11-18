package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.SupplierDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public String getNextSupplierId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Sup_Id from supplier order by Sup_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last supplier ID
            String substring = lastId.substring(1); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("S%03d", newIdIndex); // Return the new supplier ID in format Cnnn
        }
        return "S001"; // Return the default supplier ID if no data is found
    }

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into supplier values (?,?,?,?)",
                supplierDTO.getSup_Id(),
                supplierDTO.getName(),
                supplierDTO.getContact(),
                supplierDTO.getUser_Id()
        );
    }

    public ArrayList<SupplierDTO> getAllSupplier() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier");

        ArrayList<SupplierDTO> supplierDTOS = new ArrayList<>();

        while (rst.next()) {
            SupplierDTO supplierDTO= new SupplierDTO(
                    rst.getString(1),  // supplier ID
                    rst.getString(2),  // name
                    rst.getInt(3),  // contact
                    rst.getString(4) // user id

            );
            supplierDTOS.add(supplierDTO);
        }
        return supplierDTOS;
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "update supplier set Name=?, Contact=?, User_Id=? where Sup_Id=?",
                supplierDTO.getName(),
                supplierDTO.getContact(),
                supplierDTO.getUser_Id(),
                supplierDTO.getSup_Id()
        );
    }

    public boolean deleteSupplier(String Sup_Id) throws SQLException {
        return CrudUtil.execute("delete from supplier where Sup_Id=?",Sup_Id);
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Sup_Id from supplier");

        ArrayList<String> Sup_Ids = new ArrayList<>();

        while (rst.next()) {
            Sup_Ids.add(rst.getString(1));
        }

        return Sup_Ids;
    }


    public SupplierDTO findById(String selectedSupId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier where Sup_Id=?", selectedSupId);

        if (rst.next()) {
            return new SupplierDTO(
                    rst.getString(1),  // supplier ID
                    rst.getString(2), // name
                    rst.getInt(3), // contact
                    rst.getString(4) // user id
            );
        }
        return null;

    }
}
