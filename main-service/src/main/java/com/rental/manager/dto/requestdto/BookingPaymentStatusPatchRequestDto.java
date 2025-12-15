package com.rental.manager.dto.requestdto;




import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Data;


@Data
public class BookingPaymentStatusPatchRequestDto {
    private PaymentStatus paymentStatus;
}
