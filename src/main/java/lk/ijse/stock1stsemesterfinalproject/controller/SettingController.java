package lk.ijse.stock1stsemesterfinalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.stock1stsemesterfinalproject.dto.UserDTO;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private ComboBox<String> cmbuserid;

    @FXML
    private Label emaillbl;

    @FXML
    private TextField emailtxt;

    @FXML
    private Label fnamelbl;

    @FXML
    private TextField fnametxt;

    @FXML
    private ImageView iconimg;

    @FXML
    private ImageView imgpic;

    @FXML
    private Label lnamelbl;

    @FXML
    private TextField lnametxt;

    @FXML
    private Label passwordlbl;

    @FXML
    private TextField passwordtxt;

    @FXML
    private Button resetbtn;

    @FXML
    private Button savebtn;

    @FXML
    private AnchorPane settingap;

    @FXML
    private Label unamelbl;

    @FXML
    private TextField unametxt;

    @FXML
    private Label useridlbl;


    UserModel userModel = new UserModel();

    public void initialize(URL url, ResourceBundle rb) {
        unametxt.requestFocus();

        try {
            loadUserIds();
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException {
        fnametxt.setText("");
        lnametxt.setText("");
        unametxt.setText("");
        emailtxt.setText("");
        passwordtxt.setText("");

        savebtn.setDisable(false);
    }

    private void loadUserIds() throws SQLException {
        ArrayList<String> userIds = userModel.getAllUserIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(userIds);
        cmbuserid.setItems(observableList);
    }


    @FXML
    void cmbuseridOnAction(ActionEvent event)throws SQLException {
        String selectedUserId = cmbuserid.getSelectionModel().getSelectedItem();
        if (selectedUserId != null) {
            UserDTO userDTO = userModel.findById(selectedUserId);
            if (userDTO != null) {
                fnametxt.setText(userDTO.getFirstName());
                lnametxt.setText(userDTO.getLastName());
                unametxt.setText(userDTO.getUsername());
                emailtxt.setText(userDTO.getEmail());
                passwordtxt.setText(userDTO.getPassword());
            }
        }
    }

    @FXML
    void resetbtnOnAction(ActionEvent event)throws SQLException{
        cmbuserid.setValue(null);
        cmbuserid.setPromptText("Select user Id");

        fnametxt.setText("");
        lnametxt.setText("");
        unametxt.setText("");
        emailtxt.setText("");
        passwordtxt.setText("");

        refreshPage();
    }

    @FXML
    void savebtnOnAction(ActionEvent event) throws SQLException {
        String userId = String.valueOf(cmbuserid.getValue());
        String firstName = fnametxt.getText();
        String lastName = lnametxt.getText();
        String username = unametxt.getText();
        String email = emailtxt.getText();
        String password = passwordtxt.getText();

        String fnamePattern = "^[A-Za-z ]+$";
        String lnamePattern = "^[A-Za-z ]+$";
        String unamePattern = "^[a-zA-Z0-9_]{5,15}$";
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@gmail\\.com$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";

        boolean isValidFname = firstName.matches(fnamePattern);
        boolean isValidLname = lastName.matches(lnamePattern);
        boolean isValidUname = username.matches(unamePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPassword = password.matches(passwordPattern);

        fnametxt.setStyle(fnametxt.getStyle() + ";-fx-border-color:  #091057;");
        lnametxt.setStyle(lnametxt.getStyle() + ";-fx-border-color:  #091057;");
        unametxt.setStyle(unametxt.getStyle() + ";-fx-border-color:  #091057;");
        emailtxt.setStyle(emailtxt.getStyle() + ";-fx-border-color:  #091057;");
        passwordtxt.setStyle(passwordtxt.getStyle() + ";-fx-border-color:  #091057;");

        if (isValidFname && isValidLname && isValidUname && isValidEmail && isValidPassword){
            UserDTO userDTO = new UserDTO(userId, firstName, lastName, username, email, password);

            boolean isSaved = userModel.updateUser(userDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User Information saved...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save user information...!").show();
            }
        }
    }
}
