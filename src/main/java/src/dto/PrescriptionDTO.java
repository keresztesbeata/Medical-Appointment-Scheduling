package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PrescriptionDTO {

    private String medication;

    private String indications;
}
