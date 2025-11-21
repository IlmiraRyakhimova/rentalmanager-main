package com.rental.manager.dto.requestdto;




import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingPaymentStatusPatchRequestDTO {
    private PaymentStatus paymentStatus;
}
