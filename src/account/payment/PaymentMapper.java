package account.payment;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    public Payment paymentDtoToPayment(PaymentDto paymentDto) {
        return Payment
                .builder()
                .employee(paymentDto.getEmployee())
                .period(paymentDto.getPeriod())
                .salary(paymentDto.getSalary())
                .build();
    }

    public List<Payment> paymentDtosToPayments(List<PaymentDto> payments) {
        return payments
                .stream()
                .map(this::paymentDtoToPayment)
                .collect(Collectors.toList());
    }

    public PaymentDto paymentToPaymentDto(Payment payment) {
        return PaymentDto
                .builder()
                .employee(payment.getEmployee())
                .period(payment.getPeriod())
                .salary(payment.getSalary())
                .build();
    }
}