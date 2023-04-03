package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dto.Land;
import lk.ijse.dto.LandDetail;
import lk.ijse.model.LandModel;
import lk.ijse.model.LandTypeModel;
import lk.ijse.util.OpenView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LandFormController implements Initializable {
    public AnchorPane landRoot;
    public TextField txtPlan;
    public TextField txtArea;
    public ChoiceBox cbLType;
    public Label lblID;
    public static String land_id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLandType();
        generateLandId();
        if ((!(CivilManageFormController.civil == null))) {
            setCivilController();
        }
    }

    private void setCivilController() {
    }

    private void generateLandId() {
        try {
            String id = "L00"+LandModel.getNextLandId();
            lblID.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void loadLandType() {
        String[] type = new String[]{"Government","Non Government","Cultivated", "Uncultivated"};
        ObservableList<String> dataList = FXCollections.observableArrayList(type);
        cbLType.setItems(dataList);

    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String[] land_num = lblID.getText().split("L00");
        Integer type_id = LandTypeModel.getTypeId((String) cbLType.getValue());

        AddLandTypeFormController.landDetailList.add(new LandDetail(type_id, Integer.valueOf(land_num[1])));

        try {
            boolean isSaved = LandModel.save(new Land(
                    Integer.valueOf(land_num[1]), txtPlan.getText(), Double.valueOf(txtArea.getText())), AddLandTypeFormController.landDetailList, OwnershipFormController.ownerList);

            if (isSaved)
                new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !").show();
            else
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public void btnOwnerOnAction(ActionEvent actionEvent){
        land_id=lblID.getText();
        OpenView.openView("ownershipForm");

    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        OpenView.openView("registrationForm",landRoot);
    }

    public void LandTypeOnAction(ActionEvent actionEvent) {
        land_id=lblID.getText();
        OpenView.openView("addLandTypeForm");

    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        txtPlan.clear();
        txtArea.clear();
        cbLType.setValue(null);
    }
}
