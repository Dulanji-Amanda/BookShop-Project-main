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
import lk.ijse.stock1stsemesterfinalproject.dto.ItemDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.OrderDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.ItemTM;
import lk.ijse.stock1stsemesterfinalproject.model.CustomerModel;
import lk.ijse.stock1stsemesterfinalproject.model.ItemModel;
import lk.ijse.stock1stsemesterfinalproject.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private AnchorPane Apaneitem;

    @FXML
    private TableColumn<?, ?> colCiditrm;

    @FXML
    private TableColumn<?, ?> colqtyitem;

    @FXML
    private Label iemlblQty;

    @FXML
    private TableColumn<?, ?> itemcolname;

    @FXML
    private TableColumn<?, ?> itemcolprice;

    @FXML
    private Label itemidlbl;

    @FXML
    private Label itemlblname;

    @FXML
    private Label itemlbprice;

    @FXML
    private Label lblOrderID;

    @FXML
    private TableView<ItemTM> itemtbl;

    @FXML
    private Label lblItem;

    @FXML
    private Label totalPriceLbl;

    @FXML
    private TextField txritem;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtprice;

    @FXML
    private Button placeOrderBtn;

    @FXML
    private Button resetbtn;

    @FXML
    private Button addItemBtn;

    @FXML
    private ComboBox<String> cmbCustomerId;

    ItemModel itemModel = new ItemModel();
    OrderModel orderModel = new OrderModel();
    CustomerModel customerModel = new CustomerModel();

    public int qtyPrice = 0;
    public double totalPrice = 0;

    private ObservableList<ItemTM> itemList = FXCollections.observableArrayList();
    private ArrayList<ItemTM> itemListArrayList = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCiditrm.setCellValueFactory(new PropertyValueFactory<>("Item_Id"));
        itemcolname.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        colqtyitem.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        itemcolprice.setCellValueFactory(new PropertyValueFactory<>("Price"));

        itemtbl.setItems(itemList);

        try {
            String nextItemId = itemModel.getNextItemId();
            lblItem.setText(nextItemId);

            loadNextOrderId();
            loadCustomerId();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerId() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        cmbCustomerId.setItems(observableList);
    }

    @FXML
    void cmbCustomerIdOnAction(ActionEvent event) {

    }

    private void loadNextOrderId() throws SQLException {
        lblOrderID.setText(orderModel.getNextOrderId());
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        txritem.setText("");
        txtprice.setText("");
        txtQty.setText("");

        placeOrderBtn.setDisable(false);
        resetbtn.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        itemList.clear();
        itemList.addAll(itemListArrayList);
        calculateQty();
        calculatePrice();
    }

    private void calculatePrice() {
        double totalPrice = itemList.stream().mapToDouble(ItemTM::getPrice).sum();
        totalPriceLbl.setText(String.valueOf(totalPrice));
    }

    private void calculateQty() {
        qtyPrice = (int) itemList.stream().mapToDouble(ItemTM::getQty).sum();
    }

    @FXML
    void ItemMouseOnClicked(MouseEvent event) {
        ItemTM selectedItem = itemtbl.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblItem.setText(selectedItem.getItem_Id());
            txritem.setText(selectedItem.getItem_Name());
            txtprice.setText(selectedItem.getPrice().toString());
            txtQty.setText(String.valueOf(selectedItem.getQty()));

            placeOrderBtn.setDisable(true);
            resetbtn.setDisable(false);
        }
    }

    @FXML
    void addItemBtnOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItem.getText();
        String itemName = txritem.getText();
        double itemPrice = Double.parseDouble(txtprice.getText());
        int itemQty = Integer.parseInt(txtQty.getText());

        ItemTM itemTM = new ItemTM(itemId, itemName, itemQty, itemPrice);
        itemListArrayList.add(itemTM);

        setNextItemId(lblItem.getText());

        refreshTable();
        refreshPage();
    }

    private void setNextItemId(String text) {
        String substring = text.substring(1);
        int i = Integer.parseInt(substring);
        int newIdIndex = i + 1;
        lblItem.setText(String.format("I%03d", newIdIndex));
    }

    @FXML
    void placeOrderBtnOnAction(ActionEvent event) {
        if (!cmbCustomerId.getValue().isEmpty()) {
            ArrayList<ItemDTO> items = new ArrayList<>();
            for (ItemTM itemTM : itemListArrayList) {
                items.add(new ItemDTO(itemTM.getItem_Id(), itemTM.getItem_Name(), itemTM.getQty(), itemTM.getPrice()));
            }

            try {
                String orderId = lblOrderID.getText();

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrder_Id(orderId);
                orderDTO.setDescription("");
                orderDTO.setOrder_qty(qtyPrice);
                orderDTO.setCust_Id(cmbCustomerId.getValue());

                boolean isOrderSaved = itemModel.saveOrderWithItems(orderDTO, items, totalPrice);

                if (isOrderSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();
                    itemListArrayList.clear();
                    refreshTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to place order!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select the supplier id").show();
        }

    }

    @FXML
    void resetbtnOnAction(ActionEvent event) throws SQLException {
        refreshPage();
        if (!itemListArrayList.isEmpty()) {
            setNextItemId(itemListArrayList.getLast().getItem_Id());
        }
    }
}