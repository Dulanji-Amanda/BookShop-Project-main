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
import lk.ijse.stock1stsemesterfinalproject.dto.StockDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.SupplierDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.UserDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.StockTM;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.UserTM;
import lk.ijse.stock1stsemesterfinalproject.model.ItemModel;
import lk.ijse.stock1stsemesterfinalproject.model.StockModel;
import lk.ijse.stock1stsemesterfinalproject.model.SupplierModel;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockFormController implements Initializable {

    @FXML
    private AnchorPane ApaneStock;

    @FXML
    private Label StockIdlbl;

    @FXML
    private Label dateLbl;

    @FXML
    private Label StockUId;

    @FXML
    private Button btnDStock;

    @FXML
    private Button btnRStock;

    @FXML
    private Button btnSStock;

    @FXML
    private Button btnUStock;

    @FXML
    private TableColumn<StockTM, String> colNameStock;

    @FXML
    private TableColumn<UserTM, String> colStockUid;

    @FXML
    private TableColumn<StockTM, String> colStockid;

    @FXML
    private ComboBox<String> combouIDStock;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private ComboBox<String> cmbItemIds;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblnameStock;

    @FXML
    private TextField lblQty;

    @FXML
    private TableView<StockTM> tblStock;

    @FXML
    private TextField txtstockname;

    StockModel stockModel = new StockModel();
    UserModel userModel = new UserModel();
    ItemModel itemModel = new ItemModel();
    ItemDTO itemDTO = new ItemDTO();
    SupplierDTO supplierDTO = new SupplierDTO();
    SupplierModel supplierModel = new SupplierModel();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        colStockid.setCellValueFactory(new PropertyValueFactory<>("Stock_Id"));
        colNameStock.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colStockUid.setCellValueFactory(new PropertyValueFactory<>("User_Id"));

        try {
            loadUserIds();
            loadSupplierIds();
            loadItemIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        refreshTable();

       String nextStockId = stockModel.getNextStockId();
       lblStock.setText(nextStockId);
       dateLbl.setText(String.valueOf(LocalDate.now()));

        txtstockname.setText("");


        btnSStock.setDisable(false);
        btnDStock.setDisable(true);
        btnUStock.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        ArrayList<StockDTO> stockDTOS = stockModel.getAllStocks();
        ObservableList<StockTM> stockTMS = FXCollections.observableArrayList();

        for (StockDTO stockDTO : stockDTOS) {
            StockTM stockTM = new StockTM(
                    stockDTO.getStock_Id(),
                    stockDTO.getName(),
                    stockDTO.getUser_Id()
            );
            stockTMS.add(stockTM);
        }
        tblStock.setItems(stockTMS);
    }

    @FXML
    void ResetOnActionStock(ActionEvent event) throws SQLException {
        combouIDStock.setValue(null);
        combouIDStock.setPromptText("Select User_Id");
        cmbSupplierId.setValue(null);
        cmbSupplierId.setPromptText("Select Supplier_Id");
        cmbItemIds.setValue(null);
        cmbItemIds.setPromptText("Select Item_Id");
        refreshPage();
    }

    @FXML
    void SaveOnActionStock(ActionEvent event) throws SQLException {
        String Stock_Id = lblStock.getText();
        String Name = txtstockname.getText();
        String User_Id =combouIDStock.getValue();

        String namePattern = "^[A-Za-z ]+$";

        boolean isValidName = Name.matches(namePattern);

        txtstockname.setStyle(txtstockname.getStyle() + ";-fx-border-color:  #091057;");

        if (!isValidName) {
            txtstockname.setStyle(txtstockname.getStyle() + ";-fx-border-color: red;");
        }

        if (isValidName && !lblQty.getText().isEmpty()) {
            if (combouIDStock.getValue() != null && cmbSupplierId.getValue() != null && cmbItemIds.getValue() != null) {
                StockDTO stockDTO = new StockDTO(Stock_Id,Name, User_Id);

                boolean isSaved = stockModel.saveStock(stockDTO, itemDTO, supplierDTO, lblQty.getText());

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "CStock saved...!").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save stock...!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill the fields").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill the fields").show();
        }
    }
    @FXML
    void deleteOnActionStock(ActionEvent event) throws SQLException {
        String Stock_Id = lblStock.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Stock?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            boolean isDeleted = stockModel.deleteStock(Stock_Id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Stock deleted...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete stock...!").show();
            }
        }
    }

    @FXML
    void updateOnActionStock(ActionEvent event) throws SQLException {
        String Stock_Id = lblStock.getText();
        String Name = txtstockname.getText();
        String User_Id = combouIDStock.getValue();

        String namePattern = "^[A-Za-z ]+$";

        boolean isValidName = Name.matches(namePattern);

        txtstockname.setStyle(txtstockname.getStyle() + ";-fx-border-color:  #091057;");

        if (!isValidName) {
            txtstockname.setStyle(txtstockname.getStyle() + ";-fx-border-color: red;");
        }

        if (isValidName ) {
            StockDTO stockDTO = new StockDTO(Stock_Id, Name, User_Id);

            boolean isSaved = stockModel.updateStock(stockDTO, itemDTO, supplierDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Stock updated...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update stock...!").show();
            }
        }
    }

    @FXML
    void StockOnMouseCliked(MouseEvent event) throws SQLException {
        StockTM selectedItem = tblStock.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblStock.setText(selectedItem.getStock_Id());

            txtstockname.setText(selectedItem.getName());
            combouIDStock.setValue(selectedItem.getUser_Id());

            btnSStock.setDisable(true);
            btnDStock.setDisable(false);
            btnUStock.setDisable(false);
        }
    }

    private void loadUserIds() throws SQLException {
        ArrayList<String> userIds = userModel.getAllUserIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(userIds);
        combouIDStock.setItems(observableList);
    }

    private void loadItemIds() throws SQLException {
        ArrayList<String> itemIds = itemModel.getAllItemIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        cmbItemIds.setItems(observableList);
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierId.setItems(observableList);
    }

    @FXML
    void StockComboOnAction(ActionEvent event) throws SQLException {
        String selectedUserId = combouIDStock.getSelectionModel().getSelectedItem();
        if (selectedUserId != null) {
            UserDTO userDTO = userModel.findById(selectedUserId);
        }
    }

    @FXML
    void cmbItemIdsOnAction(ActionEvent event) throws SQLException {
        String selectedItemId = cmbItemIds.getSelectionModel().getSelectedItem();
        if (selectedItemId != null) {
            itemDTO = itemModel.findById(selectedItemId);
        }
    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) throws SQLException {
        String selectedSupplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
        if (selectedSupplierId != null) {
            supplierDTO = supplierModel.findById(selectedSupplierId);
        }
    }
}