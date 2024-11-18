package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.EmployeeDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public String getNextEmployeeId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Emp_Id from employee order by Emp_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last employee ID
            String substring = lastId.substring(1); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("E%03d", newIdIndex); // Return the new employee ID in format Cnnn
        }
        return "E001"; // Return the default employee ID if no data is found
    }
    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into employee values (?,?,?,?)",
                employeeDTO.getEmp_Id(),
                employeeDTO.getName(),
                employeeDTO.getContact(),
                employeeDTO.getUser_Id()
        );
    }

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee");

        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getString(1),  // employee ID
                    rst.getString(2),  // name
                    rst.getInt(3),  // contact
                    rst.getString(4) // user id

            );
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }

    public boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "update employee set Name=?, Contact=?, User_Id=? where Emp_Id=?",
                employeeDTO.getName(),
                employeeDTO.getContact(),
                employeeDTO.getUser_Id(),
                employeeDTO.getEmp_Id()
        );
    }

    public boolean deleteEmployee(String Emp_Id) throws SQLException {
        return CrudUtil.execute("delete from employee where Emp_Id=?", Emp_Id);
    }

    public ArrayList<String> getAllEmployeeIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Emp_Id from employee");

        ArrayList<String> Emp_Ids = new ArrayList<>();

        while (rst.next()) {
            Emp_Ids.add(rst.getString(1));
        }

        return Emp_Ids;
    }

    public EmployeeDTO findById(String selectedEmpId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee where Emp_Id=?", selectedEmpId);

        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getString(1),  // employee ID
                    rst.getString(2), // name
                    rst.getInt(3), // contact
                    rst.getString(4) // user id
            );
        }
        return null;
    }
}