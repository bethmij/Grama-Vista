package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class DetailDTO {
    private String function_name;
    private String user;
    private LocalTime time;
    private LocalDate date;
    private String description;
}
