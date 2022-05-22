package src.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDateRequest {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hours;
    private Integer minutes;

    public LocalDateTime convertToLocalDateTime() {
        return LocalDateTime.of(year, month, day, hours, minutes);
    }
}
