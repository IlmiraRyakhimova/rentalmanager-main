package com.rental.manager.dto.requestdto;

import com.rental.manager.service.queue.EmailTaskType;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class EmailTaskRequestDto implements Serializable {
    private String to;
    private String ownerName;
    private String ownerPassword;
    private String guestName;
    private String bookingCode;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private EmailTaskType taskType;
}
