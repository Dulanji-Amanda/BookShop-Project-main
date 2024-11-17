
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
import lk.ijse.stock1stsemesterfinalproject.dto.SupplierDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.UserDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.SupplierTM;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.UserTM;
import lk.ijse.stock1stsemesterfinalproject.model.SupplierModel;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML
    private AnchorPane ApaneSupplier;

    @FXML
    private Label SupplierIdlbl;

    @FXML
    private Button btnDSupplier;

    @FXML
    private Button btnRSupplier;

    @FXML
    private Button btnSSupplier;

    @FXML
    private Button btnUSupplier;

    @FXML
    private TableColumn<SupplierTM, String> colNameSupplier;

    @FXML
    private TableColumn<SupplierTM, String> colSupplierid;

    @FXML
    private TableColumn<UserTM, String> colUidSupplier;

    @FXML
    private TableColumn<SupplierTM, Integer> colcontactSupplier;

    @FXML
    private ComboBox<String> combouIDSupplier;

    @FXML
    private Label labelcontactSupplier;

    @FXML
    private Label lblSupplier;

    @FXML
    private Label lblblidSupplier;

    @FXML
    private Label lblnameSupplier;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private TextField txtSuppliername;

    @FXML
    private TextField txtcontactSupplier;

    SupplierModel supplierModel = new SupplierModel();

    UserModel userModel = new UserModel();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierid.setCellValueFactory(new PropertyValueFactory<>("Sup_Id"));
        colNameSupplier.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colcontactSupplier.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colUidSupplier.setCellValueFactory(new PropertyValueFactory<>("User_Id"));

        try {
            loadUserIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        String nextSupplierId = supplierModel.getNextSupplierId();
        lblSupplier.setText(nextSupplierId);

        txtSuppliername.setText("");
        txtcontactSupplier.setText("");

        btnSSupplier.setDisable(false);
        btnDSupplier.setDisable(true);
        btnUSupplier.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        ArrayList<SupplierDTO> supplierDTOS = supplierModel.getAllSupplier();
        ObservableList<SupplierTM> supplierTMS = FXCollections.observableArrayList();

        for (SupplierDTO supplierDTO : supplierDTOS) {
            SupplierTM supplierTM = new SupplierTM(
                    supplierDTO.getSup_Id(),
                    supplierDTO.getName(),
                    supplierDTO.getContact(),
                    supplierDTO.getUser_Id()
            );
            supplierTMS.add(supplierTM);
        }
        tblSupplier.setItems(supplierTMS);
    }

    @FXML
    void ResetOnActionSupplier(ActionEvent event) throws SQLException {
        combouIDSupplier.setValue(null);
        combouIDSupplier.setPromptText("Select User_Id");
        refreshPage();
    }

    @FXML
    void SaveOnActionSupplier(ActionEvent event)throws SQLException {
        String Sup_Id = lblSupplier.getText();
        String Name = txtSuppliername.getText();
        Integer Contact = Integer.valueOf(txtcontactSupplier.getText());
        String User_Id = combouIDSupplier.getValue();

        // Define regex patterns for validation
        String namePattern = "^[A-Za-z ]+$";
        String contactPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

//        Validate each field using regex patterns
        boolean isValidName = Name.matches(namePattern);
        boolean isValidContact = String.valueOf(Contact).matches(String.valueOf(contactPattern));

        // Reset input field styles
        tblSupplier.setStyle(txtSuppliername.getStyle() + ";-fx-border-color:  #091057;");
        txtcontactSupplier.setStyle(txtcontactSupplier.getStyle() + ";-fx-border-color: #091057;");

        // Highlight invalid fields in red

        if (!isValidName) {
            txtSuppliername.setStyle(txtSuppliername.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidContact) {
            txtcontactSupplier.setStyle(txtcontactSupplier.getStyle() + ";-fx-border-color: red;");
        }

        // Save supplier if all fields are valid
        if (isValidName && isValidContact ) {
            SupplierDTO supplierDTO = new SupplierDTO(Sup_Id, Name, Contact, User_Id);

            boolean isSaved = supplierModel.saveSupplier(supplierDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier saved...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save supplier...!").show();
            }
        }
    }

    @FXML
    void deleteOnActionSupplier(ActionEvent event)throws SQLException {
        String Sup_Id = lblSupplier.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supplier?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            boolean isDeleted = supplierModel.deleteSupplier(Sup_Id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete supplier...!").show();
            }
        }
    }

    @FXML
    void updateOnActionSupplier(ActionEvent event) throws SQLException {
        String Sup_Id = lblSupplier.getText();
        String Name = txtSuppliername.getText();
        Integer Contact = Integer.valueOf(txtcontactSupplier.getText());
        String User_Id = combouIDSupplier.getValue();

        // Define regex patterns for validation
        String namePattern = "^[A-Za-z ]+$";
        String contactPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

//        Validate each field using regex patterns
        boolean isValidName = Name.matches(namePattern);
        boolean isValidContact = String.valueOf(Contact).matches(String.valueOf(contactPattern));

        // Reset input field styles
        txtSuppliername.setStyle(txtSuppliername.getStyle() + ";-fx-border-color:  #091057;");
        txtcontactSupplier.setStyle(txtcontactSupplier.getStyle() + ";-fx-border-color: #091057;");

        // Highlight invalid fields in red

        if (!isValidName) {
            txtSuppliername.setStyle(txtSuppliername.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidContact) {
            txtcontactSupplier.setStyle(txtcontactSupplier.getStyle() + ";-fx-border-color: red;");
        }

        // update supplier if all fields are valid
        if (isValidName && isValidContact ) {
            SupplierDTO supplierDTO = new SupplierDTO(Sup_Id, Name, Contact, User_Id);

            boolean isSaved = supplierModel.updateSupplier(supplierDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update supplier...!").show();
            }
        }
    }
    @FXML
    void SupOnMouseClicked(MouseEvent event) {
        SupplierTM selectedItem = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblSupplier.setText(selectedItem.getSup_Id());

            txtSuppliername.setText(selectedItem.getName());
            txtcontactSupplier.setText(String.valueOf(selectedItem.getContact()));
            combouIDSupplier.setValue(selectedItem.getUser_Id());

            btnSSupplier.setDisable(true);
            btnDSupplier.setDisable(false);
            btnUSupplier.setDisable(false);
        }
    }
    private void loadUserIds() throws SQLException {
        ArrayList<String> userIds = userModel.getAllUserIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(userIds);
        combouIDSupplier.setItems(observableList);
    }

    @FXML
    void comboSupOnAction(ActionEvent event) throws SQLException {
        String selectedUserId = combouIDSupplier.getSelectionModel().getSelectedItem();
        if (selectedUserId != null) {
            UserDTO userDTO = userModel.findById(selectedUserId);
        }
    }
}
