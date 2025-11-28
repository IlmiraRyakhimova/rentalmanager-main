package com.rental.manager.dto.requestdto;




import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Data;


@Data
public class BookingPaymentStatusPatchRequestDTO {
    private PaymentStatus paymentStatus;
}
