module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;
    exports drinkshop.repository;
    exports drinkshop.service.validator;
    exports drinkshop.service;

    opens drinkshop.domain to  javafx.base;
    opens drinkshop.repository to org.mockito;
    opens drinkshop.service to org.mockito, org.junit.platform.commons;

    exports drinkshop.domain;
}