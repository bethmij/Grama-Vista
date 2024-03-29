package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.LoginBO;
import lk.ijse.bo.custom.impl.LoginBOImpl;
import lk.ijse.dto.DetailDTO;
import lk.ijse.dto.UserDTO;
import lk.ijse.dao.custom.impl.util.OpenView;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class LoginFormController {

    public PasswordField txtPass;
    public TextField txtUser;
    public static String user = null;
    @FXML
    private AnchorPane root;
    LoginBO loginBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.LOGINBO);

    public void btnOnAction(ActionEvent actionEvent) {

        List<UserDTO> userDTO = null;
        try {
            userDTO = loginBO.searchAllUser();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        boolean isTrue = false;
        for (int i = 0; i < userDTO.size(); i++) {
            if (txtUser.getText().equals(userDTO.get(i).getUser()) && BCrypt.checkpw(txtPass.getText(), userDTO.get(i).getPassword())) {
                isTrue =true;
            }
        }

        if (isTrue) {
            DetailDTO detail = new DetailDTO("Logged in", txtUser.getText(), LocalTime.now(),LocalDate.now(),"");
            try {
                loginBO.saveDetail(detail);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            OpenView.openView("dashboardForm", root);

        } else if (txtUser.getText().equals("") || txtPass.getText().equals("")) {
            txtUser.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            txtPass.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            new animatefx.animation.Shake(txtUser).play();
            new animatefx.animation.Shake(txtPass).play();
            new Alert(Alert.AlertType.ERROR, "Please enter your Username and Password ").show();
        }else {
            txtUser.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            txtPass.setStyle("-fx-border-color:  #ef0d20; -fx-font-size: 16px;");
            new animatefx.animation.Shake(txtUser).play();
            new animatefx.animation.Shake(txtPass).play();
            new Alert(Alert.AlertType.ERROR, "Incorrect Username or Password").show();
        }

        // OpenView.openView("dashboardForm",root);
    }

    public void btnSignOnAction(MouseEvent actionEvent) {
       OpenView.openView("userRegistrationForm");
    }


    public void btnForgotOnAction(MouseEvent mouseEvent) {
        List<UserDTO> userDTO = null;
        try {
            userDTO = loginBO.searchAllUser();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        int count = 0;
        for (int i = 0; i < userDTO.size(); i++) {
            if (txtUser.getText().equals(userDTO.get(i).getUser())) {
                count++;
            }
        }
        if (txtUser.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Enter your username").show();
        }else if(count==1){
            user = txtUser.getText();
            OpenView.openView("passwordForm");
        }else if (count==0){
            new Alert(Alert.AlertType.ERROR,"Invalid Username!").show();
        }


    }
}
