package health.care.booking.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AvailabilityDTO {
    String caregiverId;
    List<LocalDateTime> availableSlots;

    public AvailabilityDTO(String caregiverId, List<LocalDateTime> availableSlots) {
        this.caregiverId = caregiverId;
        this.availableSlots = availableSlots;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public List<LocalDateTime> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<LocalDateTime> availableSlots) {
        this.availableSlots = availableSlots;
    }
}
