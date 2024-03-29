package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.VoteBO;
import lk.ijse.bo.custom.impl.VoteBOImpl;
import lk.ijse.db.DBConnection;
import lk.ijse.dto.*;
import lk.ijse.dao.custom.impl.util.OpenView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static lk.ijse.controller.VoteManageFormController.voteDTO;

public class VoteFormController implements Initializable {
    public ComboBox cmbEType;
    public TextField txtCandidateCount;
    public Button btnReset;
    public TextField txtElection;
    public AnchorPane Pane;
    public List<AddCandidateDTO> addCandidateList = new ArrayList<>();
    public DatePicker dpElection;
    public Label lblCandidate;
    public Button btn1;
    public ComboBox cbCandidate;
    VoteBO voteBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.VOTEBO);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadElectionType();
        loadCandidate();
        if ((!(voteDTO == null))) {
            setVoteController();
        }
    }

    private void setVoteController() {
        cmbEType.setValue(voteDTO.getElection_type());
        if(voteDTO.getCandidate_count()!=null)
            txtCandidateCount.setText(String.valueOf(voteDTO.getCandidate_count()));
        txtElection.setText(voteDTO.getElection_id());
        cbCandidate.setDisable(true);
        dpElection.setValue(voteDTO.getDate());
        btn1.setText("Update");

    }

    private void loadCandidate() {
        try {
            List<String> id = voteBO.loadElectionID();
            ObservableList<String> dataList = FXCollections.observableArrayList();

            for (String ids : id) {
                dataList.add(ids);
            }
            cbCandidate.setItems(dataList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadElectionType() {
        String[] gender = new String[]{"Local Authorities Election"};
        ObservableList<String> dataList = FXCollections.observableArrayList(gender);
        cmbEType.setItems(dataList);
    }

    public void lblLogOnAction(MouseEvent mouseEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Logout?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            DetailDTO detail = new DetailDTO("Logged out", "bethmi", LocalTime.now(), LocalDate.now(),"");
            try {
                voteBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("loginForm", Pane);
        }
    }

    public void lblVListOnAction(MouseEvent mouseEvent) {
        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/report/ElectionReport.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | SQLException e ) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void lblVoteOnAction(MouseEvent mouseEvent) {
        OpenView.openView("voteLoginForm",Pane);
    }

    public void lblViewOnAction(MouseEvent mouseEvent) {
        LocalTime start = LocalTime.parse("17:00");
        LocalDate date = LocalDate.parse("2023-06-11");

        if(date.compareTo(LocalDate.now())==0 && LocalTime.now().isAfter(start) ) {
            OpenView.openView("voteResultForm",Pane);
        }else{
            new Alert(Alert.AlertType.ERROR, "This only eligible on "+date+" after "+start).show();
        }
    }

    public void lblAddOnAction(MouseEvent mouseEvent) {
        OpenView.openView("voteRegForm",Pane);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (btn1.getText().equals("Save")) {
            if (!(cmbEType.getValue() == null) && !txtElection.getText().equals("") && !(dpElection.getValue() == null)) {

                Integer candidateCount = null;
                if (!txtCandidateCount.getText().equals(""))
                    candidateCount = Integer.valueOf(txtCandidateCount.getText());

                VoteDTO voteDTO = new VoteDTO(txtElection.getText(), String.valueOf(cmbEType.getValue()), candidateCount, dpElection.getValue());

                boolean isSaved = false;
                try {
                    isSaved = voteBO.saveVote(voteDTO, addCandidateList);
                } catch (SQLException e) {
                    System.out.println(e);
                }

                if (isSaved)
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !").show();
                else
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
            } else {
                cmbEType.setStyle("-fx-border-color:  #ef0d20; ");
                dpElection.setStyle("-fx-border-color:  #ef0d20; ");
                txtElection.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
                new Alert(Alert.AlertType.ERROR, "Please Fill Compulsory Filed!").show();
            }
        } else if (btn1.getText().equals("Update")){

            if (!(cmbEType.getValue() == null) && !txtElection.getText().equals("") && !(dpElection.getValue() == null)) {

                Integer candidateCount = null;
                if (!txtCandidateCount.getText().equals(""))
                    candidateCount = Integer.valueOf(txtCandidateCount.getText());

                VoteDTO voteDTO = new VoteDTO(txtElection.getText(), String.valueOf(cmbEType.getValue()), candidateCount, dpElection.getValue());

                boolean isSaved = false;
                try {
                    isSaved = voteBO.updateVote(voteDTO);
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println(e);
                }

                if (isSaved)
                    new Alert(Alert.AlertType.CONFIRMATION, "Update Successfully !").show();
                else
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
            } else {
                cmbEType.setStyle("-fx-border-color:  #ef0d20; ");
                dpElection.setStyle("-fx-border-color:  #ef0d20; ");
                txtElection.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
                new Alert(Alert.AlertType.ERROR, "Please Fill Compulsory Filed!").show();
            }

        }
    }


    public void cbCandidateOnAction(ActionEvent actionEvent) {
        String name = null;
        try {
            name = voteBO.getCandidateName(Integer.valueOf(String.valueOf(cbCandidate.getValue())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to add "+name+" to the Candidate List?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            int count = 0;

            for (int i = 0; i < addCandidateList.size(); i++) {
                if (cbCandidate.getValue() != addCandidateList.get(i).getCandidate_id()) {
                    count++;
                }
            }

            if(count== addCandidateList.size()) {
                addCandidateList.add(new AddCandidateDTO(txtElection.getText(), String.valueOf(cbCandidate.getValue())));
                new Alert(Alert.AlertType.CONFIRMATION, "Saved Succesfully").show();
            }else
                new Alert(Alert.AlertType.ERROR,"Already Added!").show();
        }
    }



    public void btnResetOnAction(ActionEvent actionEvent) {
        txtElection.clear();
        cbCandidate.setValue("");
        txtCandidateCount.clear();
        cmbEType.setValue("");

    }


    public void txtNumOnKeyReleased(KeyEvent keyEvent) {
        if (!txtCandidateCount.getText().matches("^[0-9]*$")) {
            txtCandidateCount.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            lblCandidate.setText("This filed can only contain numeric values!");
        }
    }

    public void txtNumOnKeyTyped(KeyEvent keyEvent) {
        if (txtCandidateCount.getText().matches("^[0-9]*$")) {
            txtCandidateCount.setStyle("-fx-border-color:  null; -fx-font-size: 16px;");
            lblCandidate.setText("");
        }
    }

    public void lblEditOnAction(MouseEvent mouseEvent) {
        OpenView.openView("VoteManageForm",Pane);
    }
}
