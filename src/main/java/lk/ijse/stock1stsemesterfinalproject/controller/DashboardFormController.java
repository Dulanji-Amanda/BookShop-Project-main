package lk.ijse.stock1stsemesterfinalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardFormController implements Initializable {
    @FXML
    private AnchorPane ap;

    @FXML
    private ImageView homeId;

    @FXML
    private ImageView settingId;


    @FXML
    private JFXButton customerbtn;

    @FXML
    private JFXButton employeebtn;

    @FXML
    private ImageView img;

    @FXML
    private JFXButton itembtn;

    @FXML
    private Label lbl;

    @FXML
    private JFXButton ordersbtn;

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXButton paymentbtn;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton salarybtn;

    @FXML
    private JFXButton stockbtn;

    @FXML
    private JFXButton stockitembtn;

    @FXML
    private JFXButton supstockbtn;

    @FXML
    private JFXButton supplierbtn;

    @FXML
    private VBox vb1;

    @FXML
    private VBox vb2;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigateTo("/view/welcomeShop.fxml");
    }

    public void navigateTo(String fxmlPath) {
        try {
            ap.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

//  -------- Loaded anchor edges are bound to the content anchor --------
//      (1) Bind the loaded FXML to all edges of the content anchorPane
            load.prefWidthProperty().bind(ap.widthProperty());
            load.prefHeightProperty().bind(ap.heightProperty());

            ap.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }

    @FXML
    void customerbtnOnAction(ActionEvent event) {
        navigateTo("/view/customerForm.fxml");

    }

    @FXML
    void employeebtnOnAction(ActionEvent event) {
        navigateTo("/view/employeeForm.fxml");

    }

    @FXML
    void itembtnOnAction(ActionEvent event) {
        navigateTo("/view/AddItemForm.fxml");

    }

    @FXML
    void ordersbtnOnAction(ActionEvent event) {
        navigateTo("/view/itemForm.fxml");

    }

    @FXML
    void paymentbtnOnAction(ActionEvent event) {
        navigateTo("/view/paymentForm.fxml");

    }

    @FXML
    void salarybtnOnAction(ActionEvent event) {
        navigateTo("/view/ordersForm.fxml");

    }

    @FXML
    void stockbtnOnAction(ActionEvent event) {
        navigateTo("/view/stockForm.fxml");

    }

    @FXML
    void stockitembtnOnAction(ActionEvent event) {
        navigateTo("/view/stockItemDetailForm.fxml");

    }

    @FXML
    void supstockbtnOnAction(ActionEvent event) {
        navigateTo("/view/supplierStockDetailForm.fxml");

    }

    @FXML
    void supplierbtnOnAction(ActionEvent event) {
        navigateTo("/view/supplierForm.fxml");

    }
    @FXML
    void HomeOnMouseClicked(MouseEvent event) {
        navigateTo("/view/welcomeShop.fxml");

    }

    @FXML
    void settingOnMouseClicked(MouseEvent event) {
        navigateTo("/view/settingForm.fxml");

    }

}