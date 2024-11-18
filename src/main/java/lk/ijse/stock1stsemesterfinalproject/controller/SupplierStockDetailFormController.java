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
import lk.ijse.stock1stsemesterfinalproject.dto.StockDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.SupplierOrderDetailDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.SupplierOrderDetailTM;
import lk.ijse.stock1stsemesterfinalproject.model.StockModel;
import lk.ijse.stock1stsemesterfinalproject.model.SupplierModel;
import lk.ijse.stock1stsemesterfinalproject.model.SupplierOrderDetailModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupplierStockDetailFormController implements Initializable {
    @FXML
    private AnchorPane Apane;

    @FXML
    private Button btnD;

    @FXML
    private Button btnR;

    @FXML
    private Button btnS;

    @FXML
    private Button btnU;

    @FXML
    private ComboBox<String> cmbSupplierIds;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colStockId;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private Label lbl;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblStockId;

    @FXML
    private TextField lblStockName;

    @FXML
    private TableView<SupplierOrderDetailTM> tblSSD;

    SupplierModel supplierModel = new SupplierModel();
    StockModel stockModel = new StockModel();
    SupplierOrderDetailModel supplierOrderDetailModel = new SupplierOrderDetailModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("Sup_Id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colStockId.setCellValueFactory(new PropertyValueFactory<>("Stock_Id"));

        try {
            loadSupplierIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        String nextStockId = stockModel.getNextStockId();
        lbl.setText(nextStockId);
        cmbSupplierIds.getSelectionModel().clearSelection();

        lblDate.setText(String.valueOf(LocalDate.now()));
        lblStockName.setText("");

        btnS.setDisable(false);
        btnD.setDisable(true);
        btnU.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        ArrayList<SupplierOrderDetailDTO> supplierOrderDetailDTOS = supplierOrderDetailModel.getAllOrderDetails();
        ObservableList<SupplierOrderDetailTM> supplierOrderDetailTMS = FXCollections.observableArrayList();

        for (SupplierOrderDetailDTO supplierOrderDetailDTO : supplierOrderDetailDTOS) {
            SupplierOrderDetailTM supplierOrderDetailTM = new SupplierOrderDetailTM(
                    supplierOrderDetailDTO.getSup_Id(),
                    supplierOrderDetailDTO.getStock_Id(),
                    supplierOrderDetailDTO.getDate()
            );
            supplierOrderDetailTMS.add(supplierOrderDetailTM);
        }
        tblSSD.setItems(supplierOrderDetailTMS);
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierIds.setItems(observableList);
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        String stockId = lbl.getText();
        String stockName = lblStockName.getText();
        String sup_id = cmbSupplierIds.getValue();
        LocalDate date = LocalDate.parse(lblDate.getText());

        if (!cmbSupplierIds.getValue().isEmpty() && !lblStockName.getText().isEmpty()) {
            StockDTO stockDTO = new StockDTO(stockId, stockName, LoginFormController.userId);

            boolean isSaved = stockModel.saveStock(stockDTO);

            if (isSaved) {
                boolean isSavedStockSupplier = supplierOrderDetailModel.saveOrderDetail(new SupplierOrderDetailDTO(
                        date,
                        stockId,
                        sup_id
                ));
                if (isSavedStockSupplier) {
                    new Alert(Alert.AlertType.INFORMATION, "Stock saved...!").show();
                    refreshPage();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save Stock...!").show();
            }
        }
    }

    @FXML
    void cmbSupplierIdsOnAction(ActionEvent event) {

    }

    @FXML
    void deleteOnAction(ActionEvent event) {

    }

    @FXML
    void tblSSDOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void updateOnAction(ActionEvent event) {
    }
}