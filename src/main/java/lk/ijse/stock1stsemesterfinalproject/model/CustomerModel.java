package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.CustomerDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public String getNextCustomerId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Cust_Id from customer order by Cust_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last customer ID
            String substring = lastId.substring(1); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("C%03d", newIdIndex); // Return the new customer ID in format Cnnn
        }
        return "C001"; // Return the default customer ID if no data is found
    }
    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into customer values (?,?,?,?,?)",
                customerDTO.getCust_Id(),
                customerDTO.getCust_Name(),
                customerDTO.getContact(),
                customerDTO.getAddress(),
                customerDTO.getUser_Id()
                );
    }

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from customer");

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();

        while (rst.next()) {
            CustomerDTO customerDTO = new CustomerDTO(
                    rst.getString(1),  // Customer ID
                    rst.getString(2),  // name ID
                    rst.getInt(3), // contact
                    rst.getString(4),  // address
                    rst.getString(5) // user id

            );
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException {
        return CrudUtil.execute(
                "update customer set Cust_Name=?, Contact=?, Address=?, User_Id=? where Cust_Id=?",
                customerDTO.getCust_Name(),
                customerDTO.getContact(),
                customerDTO.getAddress(),
                customerDTO.getUser_Id(),
                customerDTO.getCust_Id()
        );
    }

    public boolean deleteCustomer(String Cust_Id) throws SQLException {
        return CrudUtil.execute("delete from customer where Cust_Id=?", Cust_Id);
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Cust_Id from customer");

        ArrayList<String> Cust_Ids = new ArrayList<>();

        while (rst.next()) {
            Cust_Ids.add(rst.getString(1));
        }

        return Cust_Ids;
    }

    public CustomerDTO findById(String selectedCusId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from customer where Cust_Id=?", selectedCusId);

        if (rst.next()) {
            return new CustomerDTO(
                    rst.getString(1),  // customer ID
                    rst.getString(2), // name
                    rst.getInt(3),  // contact
                    rst.getString(4), // address
                    rst.getString(5) // user id
            );
        }
        return null;
    }
}
