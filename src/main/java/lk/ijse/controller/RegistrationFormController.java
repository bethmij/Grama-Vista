package lk.ijse.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.RegistrationBO;
import lk.ijse.bo.custom.impl.RegistrationBOImpl;
import lk.ijse.dto.DetailDTO;
import lk.ijse.dao.custom.impl.util.OpenView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class RegistrationFormController {

    public AnchorPane  CivilRPane;

    public void btnHomeOnAction(ActionEvent actionEvent) { OpenView.openView ("homeRegistrationForm",CivilRPane);}

    public void btnlandOnAction(ActionEvent actionEvent) {
        OpenView.openView ("landForm",CivilRPane);
    }

    public void btnIndivOnAction(ActionEvent actionEvent)  { OpenView.openView ("individualForm",CivilRPane);}

    public void btndeadOnAction(ActionEvent actionEvent) {
        OpenView.openView ("deadPeopleForm",CivilRPane);
    }

    public void btnMaternityOnAction(ActionEvent actionEvent) {
        OpenView.openView ("MaternityRegistForm",CivilRPane);
    }

    public void btnCandidateOnAction(ActionEvent actionEvent) {OpenView.openView ("CandidateRegForm",CivilRPane);}

    public void btnDisableOnAction(ActionEvent actionEvent) { OpenView.openView ("disableRegistrationForm",CivilRPane); }

    public void btnDivisionOnAction(ActionEvent actionEvent) { OpenView.openView ("divisionRegistrationForm",CivilRPane);}

    RegistrationBO registrationBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.REGISTRATIONBO);

    @FXML
    void lblLogOnAction(MouseEvent event) {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Logout?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            DetailDTO detail = new DetailDTO("Logged out", "bethmi", LocalTime.now(), LocalDate.now(),"");
            try {
                registrationBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("loginForm",CivilRPane);
        }
    }

    @FXML
    void lblManageOnAction(MouseEvent event) {
        OpenView.openView("manageForm",CivilRPane);
    }

    @FXML
    void lblRegOnAction(MouseEvent event) {
        OpenView.openView("registrationForm",CivilRPane);
    }

    @FXML
    void lblReportOnAction(MouseEvent event) {
        OpenView.openView("reportForm",CivilRPane);
    }

    @FXML
    void lblVoteOnAction(MouseEvent event) {
        OpenView.openView("aboutUsForm",CivilRPane);
    }

}
