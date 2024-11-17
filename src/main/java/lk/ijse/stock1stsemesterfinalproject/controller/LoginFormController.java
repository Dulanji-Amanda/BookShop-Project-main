package lk.ijse.stock1stsemesterfinalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.stock1stsemesterfinalproject.dto.UserDTO;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LoginFormController {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private ImageView imgPasswordView;

    @FXML
    private Label lblCreateNewAccount;

    @FXML
    private Label lblForgotPassword;

    @FXML
    private Label lblLogin;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblError;

    @FXML
    private TextField txtPasswordVisible;

    private UserModel userModel = new UserModel();

    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {

        txtUsername.requestFocus();

        bodyPane.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                btnLogin.fire();
            }
        });
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException {
        if (checkUsernameAndPassword()) {
            loadUI("/view/dashboardForm.fxml");
        } else {
            showErrorMessage("Incorrect username or password. Please try again.");
        }
    }

    @FXML
    void imgPasswordViewOnAction(MouseEvent event) {
        if (isPasswordVisible) {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPassword.setVisible(true);
        } else {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPassword.setVisible(false);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    void lblCreateNewAccountOnAction(MouseEvent event) {

        loadUI("/view/registerForm.fxml");
    }

    @FXML
    void lblForgotPasswordOnAction(MouseEvent event) {

        loadUI("/view/forgetPasswordForm.fxml");
    }

    private boolean checkUsernameAndPassword() throws SQLException {
        List<UserDTO> allUsers = userModel.getAllUsers();

        for (UserDTO user : allUsers) {
            if (user.getUsername().equals(txtUsername.getText()) && user.getPassword().equals(txtPassword.getText())) {
                return true;
            }
        }
        return false;
    }

    private void loadUI(String resource) {
        rootPane.getChildren().clear();
        try {
            rootPane.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorMessage(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                ae -> lblError.setText("")
        ));
        timeline.play();
    }
}