package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Blob;

@Data
@AllArgsConstructor

public class CivilTM {
     String ID;
     String Name;
     String NIC;
     String Address;
     String Relation;
     Button btn;
}
