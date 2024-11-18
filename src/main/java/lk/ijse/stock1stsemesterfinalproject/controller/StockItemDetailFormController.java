package lk.ijse.stock1stsemesterfinalproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StockItemDetailFormController {

    @FXML
    private AnchorPane ApaneStockItemDetail;

    @FXML
    private Label ItemIdlblSID;

    @FXML
    private Button btnDStockItemDetail;

    @FXML
    private Button btnRStockItemDetail;

    @FXML
    private Button btnSStockItemDetail;

    @FXML
    private Button btnUStockItemDetail;

    @FXML
    private TableColumn<?, ?> colidStItemDetail;

    @FXML
    private TableColumn<?, ?> colidStockItemDetail;

    @FXML
    private Label lblIid;

    @FXML
    private Label lblSID;

    @FXML
    private Label stockIdlblSID;

    @FXML
    private TableView<?> tblStockItemDetail;

    @FXML
    void ResetOnAction(ActionEvent event) {

    }

    @FXML
    void SaveOnAction(ActionEvent event) {

    }

    @FXML
    void deleteOnAction(ActionEvent event) {

    }

    @FXML
    void tblStockItemDOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void updateOnAction(ActionEvent event) {

    }
}
