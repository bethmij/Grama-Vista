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
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.CandidateManageBO;
import lk.ijse.bo.custom.impl.CandidateManageBOImpl;
import lk.ijse.dto.*;
import lk.ijse.dto.tm.CandidateTM;
import lk.ijse.dao.custom.impl.util.OpenView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CandidateManageFormController implements Initializable {

    public Label lblDivision;
    public TableView tblDivision;
    public TableColumn colElection;
    public TableColumn colImage;
    public TableColumn colName;
    public TableColumn colNIC;
    public TableColumn colDivision;
    public TableColumn colAction;
    public AnchorPane tblDivPane;
    public ComboBox cmbID;
    private ObservableList<CandidateTM> obList = FXCollections.observableArrayList();
    public static CandidateDTO candidate;
    public static CandidateTM candidateTM;
    public static CandidateDTO candidateDetail;
    public static String id;
    CandidateManageBO candidateManageBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.CANDIDATEMANAGEBO);;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDivisionID();
        setCellValueFactory();

    }

    private void setCellValueFactory() {
        colElection.setCellValueFactory(new PropertyValueFactory<>("Election"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colDivision.setCellValueFactory(new PropertyValueFactory<>("Division"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void loadDivisionID() {
        try {
            List<String> id = candidateManageBO.loadElectionId();
            ObservableList<String> dataList = FXCollections.observableArrayList();

            for (String ids : id) {
                dataList.add(ids);
            }
            cmbID.setItems(dataList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        OpenView.openView("manageForm",tblDivPane);
    }

    public void btnGetAllOnAction(ActionEvent actionEvent) {
        

        try {
            List<CandidateDTO> candidateList  = candidateManageBO.searchAllCandidate();

            for (CandidateDTO datalist : candidateList) {
                Button btnView = new Button("View more");
                btnView.setCursor(Cursor.HAND);
                setViewBtnOnAction(btnView);

                candidateTM = new CandidateTM(datalist.getElection(),datalist.getName(),
                        datalist.getNIC(), datalist.getDivision(), btnView);

                obList.add(candidateTM);
                tblDivision.setItems(obList);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            CandidateDTO candidateDTO = candidateManageBO.searchCandidate((String) cmbID.getValue());
            candidate = new CandidateDTO(candidateDTO.getElection(),candidateDTO.getImage(),candidateDTO.getName(), candidateDTO.getNIC(),
                    candidateDTO.getDivision(),candidateDTO.getAddress(), candidateDTO.getContact(),candidateDTO.getPolitic());
            OpenView.openView("CandidateRegForm");
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws ClassNotFoundException {
        CandidateDTO candidateDTO = null;
        try {
             candidateDTO = candidateManageBO.searchCandidate((String) cmbID.getValue());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        Button btnView = new Button("View more");
        btnView.setCursor(Cursor.HAND);
        setViewBtnOnAction(btnView);

        candidateTM = new CandidateTM(candidateDTO.getElection(), candidateDTO.getName(),
                                                 candidateDTO.getNIC(), candidateDTO.getDivision(), btnView);




        obList.add(candidateTM);
        tblDivision.setItems(obList);
    }


    private void setViewBtnOnAction(Button btnView) {
        btnView.setOnAction((e) -> {

            try {
                CandidateDTO candidateDTO = candidateManageBO.searchCandidate((String) colElection.getCellData(tblDivision.getSelectionModel().getSelectedIndex()));

                id = (String) colElection.getCellData(tblDivision.getSelectionModel().getSelectedIndex());
                candidateDetail = new CandidateDTO(candidateDTO.getElection(),candidateDTO.getImage(),candidateDTO.getName(),candidateDTO.getNIC(),
                        candidateDTO.getDivision(),candidateDTO.getAddress(),candidateDTO.getContact(),candidateDTO.getPolitic());
            } catch (SQLException | ClassNotFoundException ex) {
                new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
            }
            OpenView.openView("candidateViewForm");


        });
    }



    public void cmbIDOnAction(ActionEvent actionEvent) {
        try {
            lblDivision.setText(candidateManageBO.getCandidateName((String) cmbID.getValue()));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }



    public void lblLogOnAction(MouseEvent mouseEvent) {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Logout?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            DetailDTO detail = new DetailDTO("Logged out", "bethmi", LocalTime.now(), LocalDate.now(),"");
            try {
                candidateManageBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("loginForm",tblDivPane);
        }
    }

    public void lblManageOnAction(MouseEvent mouseEvent) {
        OpenView.openView("manageForm",tblDivPane);
    }

    public void lblReportOnAction(MouseEvent mouseEvent) {
        OpenView.openView("reportForm",tblDivPane);
    }

    public void lblVoteOnAction(MouseEvent mouseEvent) {
        OpenView.openView("aboutUsForm",tblDivPane);
    }

    public void lblRegOnAction(MouseEvent mouseEvent) {
        OpenView.openView("registrationForm",tblDivPane);
    }
}
