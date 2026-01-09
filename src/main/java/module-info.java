module com.mycompany.clientside {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires com.google.gson;
    
    opens com.mycompany.clientside to javafx.fxml;
    opens com.mycompany.clientside.controllers to javafx.fxml;
    opens com.mycompany.clientside.dto to com.google.gson;


    exports com.mycompany.clientside;
}
