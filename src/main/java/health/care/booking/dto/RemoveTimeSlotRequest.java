package health.care.booking.dto;

import java.time.LocalDateTime;

public class RemoveTimeSlotRequest {
   private String caregiverId;
   private LocalDateTime selectedSlot;

    public RemoveTimeSlotRequest() {

    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public LocalDateTime getSelectedSlot() {
        return selectedSlot;
    }


    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public void setSelectedSlot(LocalDateTime selectedSlot) {
        this.selectedSlot = selectedSlot;
    }
}
