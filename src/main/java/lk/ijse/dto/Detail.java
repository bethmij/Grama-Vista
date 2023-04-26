package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor

public class Detail {
    private String function_name;
    private String user;
    private String id;
    private String name;
    private LocalTime time;
    private LocalDate date;
}
