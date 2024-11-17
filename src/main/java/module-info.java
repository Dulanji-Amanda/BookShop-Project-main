module lk.ijse.stock1stsemesterfinalproject {
    requires javafx.controls;

    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires static lombok;
    requires java.sql;
    requires net.sf.jasperreports.core;
    requires java.desktop;

    opens lk.ijse.stock1stsemesterfinalproject.dto.tm to javafx.base;
    opens lk.ijse.stock1stsemesterfinalproject.controller to javafx.fxml;
    exports lk.ijse.stock1stsemesterfinalproject;
    opens view to javafx.fxml;
}