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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.stock1stsemesterfinalproject.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterSecondFormController {

    @FXML
    private JFXButton btnRegister;

    @FXML
    private Label lblLoggedIn;

    @FXML
    private Label lblLogin;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView imgBack;

    private UserModel userModel = new UserModel();

    @FXML
    private Label lblError;

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,15}$";

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";

    @FXML
    public void initialize() {
        txtUsername.requestFocus();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws SQLException {
        saveUser();
    }

    @FXML
    void imgBackOnAction(MouseEvent event) {
        loadUI("/view/registerForm.fxml");
    }

    @FXML
    void lblLoggedInOnAction(MouseEvent event) {
        loadUI("/view/loginForm.fxml");
    }

    private void saveUser() throws SQLException {
        if (areFieldsEmpty()) {
            showErrorMessage("*Required fields cannot be empty.");
        } else if (!isValidUsername(txtUsername.getText())) {
            showErrorMessage("*Username must be 5-15 characters, containing only letters, digits, or underscores.");
        } else if (!isValidPassword(txtPassword.getText())) {
            showErrorMessage("*Password must be at least 8 characters long, contain a digit, a lowercase letter, an uppercase letter, and a special character.");
        } else if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            showErrorMessage("*Confirm password does not match.");
        } else {
            RegisterFormController.registeringUser.setUsername(txtUsername.getText());
            RegisterFormController.registeringUser.setPassword(txtConfirmPassword.getText());

            if (userModel.saveUser(RegisterFormController.registeringUser)) {
                loadUI("/view/loginForm.fxml");
            } else {
                showErrorMessage("*User not saved.");
            }
        }
    }
    private boolean areFieldsEmpty() {
        return txtPassword.getText().isEmpty() || txtConfirmPassword.getText().isEmpty() || txtUsername.getText().isEmpty();
    }

        private boolean isValidUsername(String username) {
            return username.matches(USERNAME_PATTERN);
        }

        private boolean isValidPassword(String password) {
            return password.matches(PASSWORD_PATTERN);
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