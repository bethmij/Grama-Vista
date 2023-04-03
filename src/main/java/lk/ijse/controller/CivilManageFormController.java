package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dto.Candidate;
import lk.ijse.dto.CandidateDTO;
import lk.ijse.dto.Civil;
import lk.ijse.dto.CivilDTO;
import lk.ijse.dto.tm.CandidateTM;
import lk.ijse.dto.tm.CivilTM;
import lk.ijse.model.CandidateModel;
import lk.ijse.model.CivilModel;
import lk.ijse.util.OpenView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CivilManageFormController implements Initializable {
    public AnchorPane tblDivPane;
    public Label lblName;
    public TableView tbl;
    public TableColumn colID;
    public TableColumn colImage;
    public TableColumn colName;
    public TableColumn colNIC;
    public TableColumn colAddress;
    public TableColumn colAction;
    public ComboBox cmbID;
    private ObservableList<CivilTM> obList = FXCollections.observableArrayList();
    public static Civil civil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactory();
        loadCivilId();
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("Image"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void loadCivilId() {
        List<String> id = null;
        try {
            id = CivilModel.loadCivilId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<String> dataList = FXCollections.observableArrayList();

        for (String ids : id) {
            dataList.add(ids);
        }
        cmbID.setItems(dataList);
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        OpenView.openView("manageForm",tblDivPane);
    }


    public void btnGetAllOnAction(ActionEvent actionEvent) {

        try {
            List<CivilDTO> civilDTOList  = CivilModel.searchAll();

            for (CivilDTO datalist : civilDTOList) {
                Button btnView = new Button("View more");
                btnView.setCursor(Cursor.HAND);
                setViewBtnOnAction(btnView);

                CivilTM civilTM = new CivilTM(datalist.getID(),datalist.getImage(),datalist.getName(),
                        datalist.getNic(),datalist.getAddress(),btnView);
                obList.add(civilTM);
                tbl.setItems(obList);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            CivilDTO civilDTO = CivilModel.search((String) cmbID.getValue());
            civil= new Civil(civilDTO.getID(), civilDTO.getName(),civilDTO.getNic(),civilDTO.getAddress(),LocalDate.parse(civilDTO.getDob()),civilDTO.getGender(),
                            civilDTO.getMarriage(),civilDTO.getRelation(),civilDTO.getEducation(),civilDTO.getSchool(),civilDTO.getOccupation(),civilDTO.getWork(),civilDTO.getSalary());
            OpenView.openView("individualForm");
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        CivilDTO civilDTO = null;
        try {
            civilDTO = CivilModel.search((String) cmbID.getValue());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        Button btnView = new Button("View more");
        btnView.setCursor(Cursor.HAND);
        setViewBtnOnAction(btnView);



        CivilTM civilTM = new CivilTM(civilDTO.getID(),civilDTO.getImage(),civilDTO.getName(),
                            civilDTO.getNic(),civilDTO.getAddress(),btnView);
        obList.add(civilTM);
        tbl.setItems(obList);
    }

    private void setViewBtnOnAction(Button btnView) {
        /*btnView.setOnAction((e) -> {
           OpenView.openView("candidateManageForm2");

           new CandidateManageForm2Controller().setTbl2(candidateDTO);
        });*/
    }



    public void cmbIDOnAction(ActionEvent actionEvent) {
        try {
            lblName.setText(CivilModel.getName((String) cmbID.getValue()));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
