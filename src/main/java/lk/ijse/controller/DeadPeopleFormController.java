package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.DeadPeopleBO;
import lk.ijse.bo.custom.impl.DeadPeopleBOImpl;
import lk.ijse.dto.DeadDTO;
import lk.ijse.dto.DetailDTO;
import lk.ijse.dao.custom.impl.util.OpenView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static lk.ijse.controller.DeadManageFormController.dead;

public class DeadPeopleFormController implements Initializable {
    public AnchorPane deadPane;
    public ComboBox cmbCivil;
    public DatePicker dtpDate;
    public Label lblName;
    public Label lblID;
    public Button btn1;
    DeadPeopleBO deadPeopleBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.DEADPEOPLEBO);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCivilId();
        generateNextId();
        if ((!(dead == null))) {
            setDeadController();
        }
    }

    private void setDeadController() {
        cmbCivil.setValue(dead.getCivil_id());
        dtpDate.setValue(dead.getDate());
        lblName.setText(dead.getName());
        lblID.setText(dead.getDead_id());
        btn1.setText("Update");
    }

    private void generateNextId() {
        try {
            lblID.setText("DD00"+ deadPeopleBO.getNextDeadId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadCivilId() {
        List<String> id = null;
        try {
            id = deadPeopleBO.loadCivilId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        ObservableList<String> dataList = FXCollections.observableArrayList();

        for (String ids : id) {
            dataList.add("C00"+ids);
        }
        cmbCivil.setItems(dataList);
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        OpenView.openView("registrationForm",deadPane);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {

        if(btn1.getText().equals("Save")) {
            if (!(cmbCivil.getValue() == null) && !(dtpDate.getValue() == null)) {
                String id = (String) cmbCivil.getValue();
                String[] civil_id = id.split("C00");
                String[] reg_id = lblID.getText().split("DD00");
                String division_id = deadPeopleBO.getDivisionId(Integer.valueOf(civil_id[1]));

                try {
                    boolean isSaved = deadPeopleBO.saveDead(new DeadDTO(reg_id[1], civil_id[1], lblName.getText(), dtpDate.getValue(),null,division_id));

                    if (isSaved) {
                        DetailDTO detail = new DetailDTO("Registration", "bethmi", LocalTime.now(), LocalDate.now(), "Registering dead_people id - " + lblID.getText() + " \nname - " + lblName.getText());
                        try {
                            deadPeopleBO.saveDetail(detail);
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                        new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !").show();
                    }else
                        new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }else{
                cmbCivil.setStyle("-fx-border-color:  #ef0d20; ");
                dtpDate.setStyle("-fx-border-color:  #ef0d20; ");
                new Alert(Alert.AlertType.ERROR, "Please Fill Compulsory Filed!").show();
            }
        }else if(btn1.getText().equals("Update")){
            if (!(cmbCivil.getValue() == null) && !(dtpDate.getValue() == null)) {
                try {

                    boolean isUpdate = deadPeopleBO.updateDead(new DeadDTO(lblID.getText(), (String) cmbCivil.getValue(), lblName.getText(), dtpDate.getValue(),null));
                    if (isUpdate)
                        new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully !").show();
                    else
                        new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }else{
                cmbCivil.setStyle("-fx-border-color:  #ef0d20; ");
                dtpDate.setStyle("-fx-border-color:  #ef0d20; ");
                new Alert(Alert.AlertType.ERROR, "Please Fill Compulsory Filed!").show();
            }
        }


    }

    public void cmbCivilOnAction(ActionEvent actionEvent) {

        String id = (String) cmbCivil.getValue();
        String[] strings = id.split("C00");

        try {

            lblName.setText(deadPeopleBO.searchCivilByID(strings[1]));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    public void btnResetOnAction(ActionEvent actionEvent) {
         dtpDate.setValue(null);
         lblName.setText("");
    }

    @FXML
    void lblLogOnAction(MouseEvent event) {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Logout?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            DetailDTO detail = new DetailDTO("Logged out", "bethmi", LocalTime.now(), LocalDate.now(),"");
            try {
                deadPeopleBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("loginForm",deadPane);
        }
    }

    @FXML
    void lblManageOnAction(MouseEvent event) {
        OpenView.openView("manageForm",deadPane);
    }


    @FXML
    void lblRegOnAction(MouseEvent event) {
        OpenView.openView("registrationForm",deadPane);
    }

    @FXML
    void lblReportOnAction(MouseEvent event) {
        OpenView.openView("reportForm",deadPane);
    }

    @FXML
    void lblVoteOnAction(MouseEvent event) {
        OpenView.openView("aboutUsForm",deadPane);
    }
}
