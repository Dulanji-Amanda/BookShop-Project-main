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
import lk.ijse.stock1stsemesterfinalproject.dto.tm.StockTM;
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
    private Button btnR;

    @FXML
    private ComboBox<String> cmbSupplierIds;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colStockId;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblStockId;

    @FXML
    private TableView<SupplierOrderDetailTM> tblSSD;

    SupplierOrderDetailModel supplierOrderDetailModel = new SupplierOrderDetailModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("Stock_Id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colStockId.setCellValueFactory(new PropertyValueFactory<>("Sup_Id"));

        try {
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        cmbSupplierIds.getSelectionModel().clearSelection();

        lblDate.setText(String.valueOf(LocalDate.now()));
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

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void cmbSupplierIdsOnAction(ActionEvent event) {

    }

    @FXML
    void tblSSDOnMouseClicked(MouseEvent event) {
        SupplierOrderDetailTM selectedItem = tblSSD.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblStockId.setText(selectedItem.getSup_Id());
            cmbSupplierIds.setValue(selectedItem.getStock_Id());
            lblDate.setText(String.valueOf(selectedItem.getDate()));

            btnR.setDisable(false);
        }
    }
}