package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.UserManageBO;
import lk.ijse.bo.custom.impl.UserManageBOImpl;
import lk.ijse.dto.*;
import lk.ijse.dto.tm.UserTM;
import lk.ijse.dao.custom.impl.util.OpenView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserManageFormController implements Initializable {
    public AnchorPane tblDivPane;
    public ChoiceBox cbEmployee;
    public Label lblName;
    public TableView tblDivision;
    public TableColumn colEmployee;
    public TableColumn colDivision;
    public TableColumn colName;
    public TableColumn colNIC;
    public TableColumn colUser;
    public TableColumn colLand;
    public TableColumn colAction;
    private ObservableList<UserTM> obList = FXCollections.observableArrayList();
    public static UserDTO user;
    UserManageBO userManageBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.USERMANAGEBO);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEmployeeId();
        setCellValueFactory();
    }

    private void loadEmployeeId() {
        try {
            List<String> id = userManageBO.loadEmployeeId();
            ObservableList<String> dataList = FXCollections.observableArrayList();

            for (String ids : id) {
                dataList.add(ids);
            }
            cbEmployee.setItems(dataList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDivision.setCellValueFactory(new PropertyValueFactory<>("Division"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        OpenView.openView("manageForm",tblDivPane);
    }


    public void btnGetAllOnAction(ActionEvent actionEvent) {
        try {
            List<UserDTO> userDTOS  = userManageBO.searchAllUser();

            for (UserDTO datalist : userDTOS) {
                Button btnDelete = new Button("Delete");
                btnDelete.setCursor(Cursor.HAND);
                setDeleteBtnOnAction(btnDelete);

                UserTM userTM = new UserTM(datalist.getEmployee(), datalist.getDivision(), datalist.getName(), datalist.getNic(), datalist.getUser(), btnDelete);
                obList.add(userTM);
                tblDivision.setItems(obList);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            UserDTO userDTO = userManageBO.searchUser((String)cbEmployee.getValue());
            user= new UserDTO(userDTO.getEmployee(),userDTO.getDivision(),userDTO.getName() , userDTO.getNic(), userDTO.getUser(), userDTO.getPassword(),
                    userDTO.getDob(),userDTO.getAge(),userDTO.getEmDate(),userDTO.getContact(),userDTO.getEmail());

            OpenView.openView("userRegistrationForm");
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        UserDTO userDTO = null;
        try {
           userDTO = userManageBO.searchUser((String)cbEmployee.getValue());

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        Button btnDelete = new Button("Delete");
        btnDelete.setCursor(Cursor.HAND);
        setDeleteBtnOnAction(btnDelete);

        UserTM userTM = new UserTM(userDTO.getEmployee(), userDTO.getDivision(), userDTO.getName(), userDTO.getNic(), userDTO.getUser(), btnDelete);
        obList.add(userTM);
        tblDivision.setItems(obList);
    }

    private void setDeleteBtnOnAction(Button btnDelete) {
        btnDelete.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    boolean isDeleted = userManageBO.deleteUser((String) colEmployee.getCellData(tblDivision.getSelectionModel().getSelectedIndex()));

                    if(isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION,"Deleted!" ).show();
                        obList.remove(tblDivision.getSelectionModel().getSelectedIndex());
                        tblDivision.refresh();
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                }

            }

        });
    }

    @FXML
    void lblLogOnAction(MouseEvent event) {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Logout?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            DetailDTO detail = new DetailDTO("Logged out", "bethmi", LocalTime.now(), LocalDate.now(),"");
            try {
                userManageBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("loginForm", tblDivPane);
        }
    }

    @FXML
    void lblManageOnAction(MouseEvent event) {
        OpenView.openView("manageForm",tblDivPane);
    }

    @FXML
    void lblRegOnAction(MouseEvent event) {
        OpenView.openView("registrationForm",tblDivPane);
    }

    @FXML
    void lblReportOnAction(MouseEvent event) {
        OpenView.openView("reportForm",tblDivPane);
    }

    @FXML
    void lblVoteOnAction(MouseEvent event) {
        OpenView.openView("aboutUsForm",tblDivPane);
    }
}
