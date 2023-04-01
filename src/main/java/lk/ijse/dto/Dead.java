package lk.ijse.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor

public class Dead {
    private String civil_ID;
    private String name;
    private LocalDate date;
}
