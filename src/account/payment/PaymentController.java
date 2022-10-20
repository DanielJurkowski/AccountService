package account.payment;

import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("api/acct/payments")
    public StatusDto addPayments(@RequestBody @UniqueElements List<@Valid PaymentDto> payments) {
        return paymentService.addPayments(payments);
    }

    @PutMapping("api/acct/payments")
    public StatusDto updatePayments(@RequestBody @Valid PaymentDto paymentDto) {
        return paymentService.updatePayment(paymentDto);
    }

    @GetMapping("api/empl/payment")
    public Object getPayment(@RequestParam(required = false) @DateTimeFormat(pattern = "MM-yyyy") Calendar period) {
        if (period != null) {
            return paymentService.getCurrentEmployeeDataByPeriod(calendarToYearMonth(period));
        } else {
            return paymentService.getAllCurrentEmployeeData();
        }
    }

    private YearMonth calendarToYearMonth(Calendar calendar) {
        return YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }
}
