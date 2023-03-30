package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lk.ijse.dto.MultiResidence;
import lk.ijse.model.ResidenceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddResidenceFormController implements Initializable {
    public TextField txtCivil;
    public ChoiceBox cbResidence;
    public static List<MultiResidence> residenceList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadResidenceID();
    }

    private void loadResidenceID() {
        try {
            List<String> id = ResidenceModel.loadResidenceID();
            ObservableList<String> dataList = FXCollections.observableArrayList();

            for (String ids : id) {
                dataList.add(ids);
            }
            cbResidence.setItems(dataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent)  {

        String[] strings = txtCivil.getText().split("C00");
        residenceList.add(new MultiResidence(strings[1],(String) cbResidence.getValue()));
    }


}
