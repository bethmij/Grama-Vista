package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dto.Civil;
import lk.ijse.dto.Civil1;
import lk.ijse.dto.Contact;
import lk.ijse.dto.MultiResidence;
import lk.ijse.model.*;
import lk.ijse.util.OpenView;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class individualForm2Controller implements Initializable {

    public AnchorPane indiroot2;
    public TextField txtSchool;
    public TextField txtSalary;
    public TextField txtWork;
    public TextField txtOccupation;
    public ChoiceBox cbEdu;
    public TextField txtContact2;
    public TextField txtContact1;
    public Label lblContact;
    public Label lblContact1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGender();
    }

    private void loadGender() {
        String[] education = new String[]{"Student","Employed","Unemployed"};
        ObservableList<String> dataList = FXCollections.observableArrayList(education);
        cbEdu.setItems(dataList);
    }


    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        Civil civil =new Civil(IndividualFormController.civil1.getId(),IndividualFormController.civil1.getName(), IndividualFormController.civil1.getNic(), IndividualFormController.civil1.getAddress(),
                IndividualFormController.civil1.getDob(), IndividualFormController.civil1.getGender(), IndividualFormController.civil1.getMarriage(),
                IndividualFormController.civil1.getRelation(),(String) cbEdu.getValue(),txtSchool.getText(),txtOccupation.getText(),txtWork.getText(),Double.valueOf(txtSalary.getText()));

        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact(IndividualFormController.civil1.getId(), Integer.valueOf(txtContact1.getText())));
        contactList.add(new Contact(IndividualFormController.civil1.getId(), Integer.valueOf(txtContact2.getText())));

        AddResidenceFormController.residenceList.add(new MultiResidence( IndividualFormController.civil1.getResidence(),IndividualFormController.civil1.getId()));

        boolean isSaved = CivilResidenceModel.save( civil,contactList,AddResidenceFormController.residenceList);

        if (isSaved)
            new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !").show();
        else
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws SQLException {
        OpenView.openView("individualForm",indiroot2);

    }


    public void btnImageOnAction(ActionEvent actionEvent) {

        Window window = ((Node) (actionEvent.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);
        actionEvent.consume();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            boolean isUploaded = CivilModel.upload(String.valueOf((CivilModel.getNextId()-1)),in);

            if (isUploaded)
                new Alert(Alert.AlertType.CONFIRMATION, "Image Uploaded Successfully !").show();
            else
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtContact1OnKeyReleased(KeyEvent keyEvent) {
        if (!txtContact1.getText().matches("^[0-9]*$")) {
            txtContact1.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            lblContact.setText("This filed can only contain numeric values!");
        }
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        if (!txtContact2.getText().matches("^[0-9]*$")) {
            txtContact2.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            lblContact.setText("This filed can only contain numeric values!");
        }
    }

    public void txtContact1OnKeyTyped(KeyEvent keyEvent) {
        if (txtContact1.getText().matches("^[0-9]*$")) {
            txtContact1.setStyle("-fx-border-color:  null; -fx-font-size: 16px;");
            lblContact.setText("");
        }
    }

    public void txtContactOnKeyTyped(KeyEvent keyEvent) {
        if (txtContact2.getText().matches("^[0-9]*$")) {
            txtContact2.setStyle("-fx-border-color: null; -fx-font-size: 16px;");
            lblContact.setText("");
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
    }
}
