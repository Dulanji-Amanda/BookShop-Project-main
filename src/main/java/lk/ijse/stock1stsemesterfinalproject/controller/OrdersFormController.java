package lk.ijse.stock1stsemesterfinalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.stock1stsemesterfinalproject.dto.CustomerDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.OrderDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.PaymentDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.OrderTM;
import lk.ijse.stock1stsemesterfinalproject.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrdersFormController implements Initializable {

    @FXML
    private AnchorPane OrderApane;

    @FXML
    private TableView<OrderTM> OrderTbl;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotalAmount;

    @FXML
    private ComboBox<String> custIdComboOrder;

    @FXML
    private Label dateLbl;

    @FXML
    private Label lblItemId;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblOrderQty;

    @FXML
    private Label lblOrderedQty;

    @FXML
    private Button orderbtnD;

    @FXML
    private Button orderbtnR;

    @FXML
    private Button orderbtnU;

    @FXML
    private TextField txtOrderDesc;

    OrderModel orderModel = new OrderModel();
    CustomerModel customerModel = new CustomerModel();
    ItemModel itemModel = new ItemModel();
    OrderItemDetailModel orderItemDetailModel = new OrderItemDetailModel();
    PaymentModel paymentModel = new PaymentModel();

    public double itemPrice = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("Order_Id"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("Item_Id"));
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("Payment_Id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("Customer_Id"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        try {
            loadCustomerIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerIds() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        custIdComboOrder.setItems(observableList);
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        txtOrderDesc.setText("");
        lblOrderedQty.setText("");
        lblItemPrice.setText("");
        lblOrderId.setText("");
        custIdComboOrder.getSelectionModel().clearSelection();
        lblItemId.setText("");
        dateLbl.setText("");
        lblItemName.setText("");

        orderbtnD.setDisable(true);
        orderbtnU.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        ArrayList<OrderDTO> orderDTOS = orderModel.getAllOrders();
        ObservableList<OrderTM> orderTMS = FXCollections.observableArrayList();

        if (orderDTOS.isEmpty()) {
            return;
        }

        for (OrderDTO orderDTO : orderDTOS) {
            String paymentId = null;
            PaymentDTO paymentDTO = paymentModel.getPaymentByOrderId(orderDTO.getOrder_Id());

            if (paymentDTO != null) {
                paymentId = paymentDTO.getPaymentId();
            } else {
                System.out.println("No payment found for order ID: " + orderDTO.getOrder_Id());
            }

            OrderTM orderTM = new OrderTM(
                    orderDTO.getOrder_Id(),
                    orderItemDetailModel.findById(orderDTO.getOrder_Id()).getItemId(),
                    paymentId,
                    orderDTO.getCust_Id(),
                    orderDTO.getOrder_qty(),
                    orderItemDetailModel.findById(orderDTO.getOrder_Id()).getDate(),
                    orderItemDetailModel.findById(orderDTO.getOrder_Id()).getAmount()
            );
            orderTMS.add(orderTM);
        }
        OrderTbl.setItems(orderTMS);
    }

    @FXML
    void OrderTblOnMouseClicked(MouseEvent event) throws SQLException {
        OrderTM selectedOrder = (OrderTM) OrderTbl.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            lblOrderId.setText(selectedOrder.getOrder_Id());
            txtOrderDesc.setText(orderModel.findById(selectedOrder.getOrder_Id()).getDescription());
            lblOrderedQty.setText(String.valueOf(selectedOrder.getQty()));
            lblItemPrice.setText(String.valueOf(itemModel.findById(selectedOrder.getItem_Id()).getPrice()));
            custIdComboOrder.setValue(selectedOrder.getCustomer_Id());
            lblItemId.setText(selectedOrder.getItem_Id());
            lblItemName.setText(String.valueOf(itemModel.findById(selectedOrder.getItem_Id()).getItem_Name()));
            dateLbl.setText(String.valueOf(orderItemDetailModel.findById(selectedOrder.getOrder_Id()).getDate()));

            orderbtnU.setDisable(false);
            orderbtnD.setDisable(false);
        }
    }

    @FXML
    void OrderResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void OrderdeleteOnAction(ActionEvent event) throws SQLException {
        String order_id = lblOrderId.getText();
        boolean isDeleted = orderModel.deleteOrder(order_id);

        if (isDeleted) {
            new Alert(Alert.AlertType.INFORMATION, "Order deleted...!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete order...!").show();
        }
    }

    @FXML
    void OrderupdateOnAction(ActionEvent event) throws SQLException {
        String order_id = lblOrderId.getText();
        String description = txtOrderDesc.getText();
        int qty = Integer.parseInt(lblOrderedQty.getText());
        String cust_id = custIdComboOrder.getValue();

        OrderDTO orderDTO = new OrderDTO(order_id, description, qty, cust_id);

        boolean isUpdated = orderModel.updateOrder(orderDTO);

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Order updated...!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update order...!").show();
        }
    }
    @FXML
    void custIdComboOrderOnAction(ActionEvent event) throws SQLException {
        String selectCustomerIds = String.valueOf(custIdComboOrder.getSelectionModel().getSelectedItem());
        if (selectCustomerIds != null) {
            CustomerDTO customerDTO = customerModel.findById(selectCustomerIds);
        }
    }
}