package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class Owner {
    private String land_id;
    private String civil_id;
    private String lot_num;
    private Double percentage;
}