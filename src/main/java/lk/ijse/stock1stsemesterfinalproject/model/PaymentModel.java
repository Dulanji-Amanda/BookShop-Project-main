package lk.ijse.stock1stsemesterfinalproject.model;

import lk.ijse.stock1stsemesterfinalproject.dto.PaymentDTO;
import lk.ijse.stock1stsemesterfinalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentModel {

    public String getNextPaymentId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select Payment_Id from payment order by Payment_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }

    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException {
        return CrudUtil.execute("INSERT INTO payment (Payment_Id, Amount, Contact, Payment_Date, Order_Id) VALUES (?,?,?,?,?)",
                paymentDTO.getPaymentId(), paymentDTO.getAmount(), paymentDTO.getContact(), paymentDTO.getPaymentDate(), paymentDTO.getOrderId());
    }

    public PaymentDTO getPaymentByOrderId(String orderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM payment WHERE Order_Id=?", orderId);
        if (rst.next()) {
            return new PaymentDTO(
                    rst.getString("Payment_Id"),
                    rst.getDouble("Amount"),
                    rst.getInt("Contact"),
                    rst.getDate("Payment_Date").toLocalDate(),
                    rst.getString("Order_Id")
            );
        }
        return null;
    }
}
