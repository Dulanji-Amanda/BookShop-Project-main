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
import lk.ijse.stock1stsemesterfinalproject.dto.EmployeeDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.UserDTO;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.EmployeeTM;
import lk.ijse.stock1stsemesterfinalproject.dto.tm.UserTM;
import lk.ijse.stock1stsemesterfinalproject.model.EmployeeModel;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML
    private AnchorPane ApaneEmp;

    @FXML
    private Button empbtnD;

    @FXML
    private Button empbtnR;

    @FXML
    private Button empbtnS;

    @FXML
    private Button empbtnU;

    @FXML
    private TableColumn<EmployeeTM, String> empcolCid;

    @FXML
    private TableColumn<EmployeeTM, String> empcolName;

    @FXML
    private TableColumn<UserTM, String> empcolUid;

    @FXML
    private TableColumn<EmployeeTM, Integer> empcolcontact;

    @FXML
    private ComboBox<String> empcombouID;

    @FXML
    private Label empidlbl;

    @FXML
    private Label emplabelcontact;

    @FXML
    private Label emplbl;

    @FXML
    private Label emplblid;

    @FXML
    private Label emplblname;

    @FXML
    private TableView<EmployeeTM> emptbl;

    @FXML
    private TextField emptxtcontact;

    @FXML
    private TextField txtempname;

    EmployeeModel employeeModel = new EmployeeModel();

    UserModel userModel = new UserModel();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        empcolCid.setCellValueFactory(new PropertyValueFactory<>("Emp_Id"));
        empcolName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        empcolcontact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        empcolUid.setCellValueFactory(new PropertyValueFactory<>("User_Id"));

        try {
            loadUserIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        refreshTable();

        String nextEmployeeID = employeeModel.getNextEmployeeId();
        emplbl.setText(nextEmployeeID);

        txtempname.setText("");
        emptxtcontact.setText("");

        empbtnS.setDisable(false);
        empbtnD.setDisable(true);
        empbtnU.setDisable(true);
    }

    private void refreshTable() throws SQLException {
        ArrayList<EmployeeDTO> employeeDTOS = employeeModel.getAllEmployees();
        ObservableList<EmployeeTM> employeeTMS = FXCollections.observableArrayList();

        for (EmployeeDTO employeeDTO : employeeDTOS) {
            EmployeeTM employeeTM = new EmployeeTM(
                    employeeDTO.getEmp_Id(),
                    employeeDTO.getName(),
                    employeeDTO.getContact(),
                    employeeDTO.getUser_Id()
            );
            employeeTMS.add(employeeTM);
        }
        emptbl.setItems(employeeTMS);
    }

    @FXML
    void empResetOnAction(ActionEvent event) throws SQLException {
        empcombouID.setValue(null);
        empcombouID.setPromptText("Select User_Id");
        refreshPage();
    }

    @FXML
    void empSaveOnAction(ActionEvent event) throws SQLException {
        String Emp_Id = emplbl.getText();
        String Name = txtempname.getText();
        Integer Contact = Integer.valueOf(emptxtcontact.getText());
        String User_Id = empcombouID.getValue();

        // Define regex patterns for validation
        String namePattern = "^[A-Za-z ]+$";
        String contactPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

//        Validate each field using regex patterns
        boolean isValidName = Name.matches(namePattern);
        boolean isValidContact = String.valueOf(Contact).matches(String.valueOf(contactPattern));

        // Reset input field styles
        txtempname.setStyle(txtempname.getStyle() + ";-fx-border-color:  #091057;");
        emptxtcontact.setStyle(emptxtcontact.getStyle() + ";-fx-border-color: #091057;");

        // Highlight invalid fields in red

        if (!isValidName) {
            txtempname.setStyle(txtempname.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidContact) {
            emptxtcontact.setStyle(emptxtcontact.getStyle() + ";-fx-border-color: red;");
        }

        // Save employee if all fields are valid
        if (isValidName && isValidContact ) {
            EmployeeDTO employeeDTO = new EmployeeDTO(Emp_Id, Name, Contact, User_Id);

            boolean isSaved = employeeModel.saveEmployee(employeeDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee saved...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save employee...!").show();
            }
        }
    }

    @FXML
    void empdeleteOnAction(ActionEvent event) throws SQLException {
        String Emp_Id = emplbl.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            boolean isDeleted = employeeModel.deleteEmployee(Emp_Id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete employee...!").show();
            }
        }
    }

    @FXML
    void empupdateOnAction(ActionEvent event) throws SQLException {
        String Emp_Id = emplbl.getText();
        String Name = txtempname.getText();
        Integer Contact = Integer.valueOf(emptxtcontact.getText());
        String User_Id = empcombouID.getValue();

        // Define regex patterns for validation
        String namePattern = "^[A-Za-z ]+$";
        String contactPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

//        Validate each field using regex patterns
        boolean isValidName = Name.matches(namePattern);
        boolean isValidContact = String.valueOf(Contact).matches(String.valueOf(contactPattern));

        // Reset input field styles
        txtempname.setStyle(txtempname.getStyle() + ";-fx-border-color:  #091057;");
        emptxtcontact.setStyle(emptxtcontact.getStyle() + ";-fx-border-color: #091057;");

        // Highlight invalid fields in red

        if (!isValidName) {
            txtempname.setStyle(txtempname.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidContact) {
            emptxtcontact.setStyle(emptxtcontact.getStyle() + ";-fx-border-color: red;");
        }

        // update employee if all fields are valid
        if (isValidName && isValidContact ) {
            EmployeeDTO employeeDTO = new EmployeeDTO(Emp_Id, Name, Contact, User_Id);

            boolean isSaved = employeeModel.updateEmployee(employeeDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update employee...!").show();
            }
        }
    }

    @FXML
    void onClickedTableEmp(MouseEvent event) {
        EmployeeTM selectedItem = emptbl.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            emplbl.setText(selectedItem.getEmp_Id());

            txtempname.setText(selectedItem.getName());
            emptxtcontact.setText(String.valueOf(selectedItem.getContact()));
            empcombouID.setValue(selectedItem.getUser_Id());

            empbtnS.setDisable(true);
            empbtnD.setDisable(false);
            empbtnU.setDisable(false);
        }
    }

    private void loadUserIds() throws SQLException {
        ArrayList<String> userIds = userModel.getAllUserIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(userIds);
        empcombouID.setItems(observableList);
    }

    @FXML
    void comboUidEmpOnAction(ActionEvent event) throws SQLException {
        String selectedUserId = String.valueOf(empcombouID.getSelectionModel().getSelectedItem());
        if (selectedUserId != null) {
            UserDTO userDTO = userModel.findById(selectedUserId);
        }
    }
}