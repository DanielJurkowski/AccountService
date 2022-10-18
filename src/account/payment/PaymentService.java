package account.payment;

import account.user.CurrentUser;
import account.user.User;
import account.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private PaymentMapper paymentMapper;
    private CurrentUser currentUser;

    @Transactional
    public StatusDto addPayments(List<PaymentDto> paymentDtos) {
        List<Payment> payments = paymentMapper.paymentDtosToPayments(paymentDtos);

        for (var payment : payments) {
            if (!userRepository.existsByEmailIgnoreCase(payment.getEmployee())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee with specified email not found");
            }

            if (paymentRepository.existsByEmployeeIgnoreCaseAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record already created");
            }

            paymentRepository.save(payment);
        }

        return new StatusDto("Added successfully!");
    }

    @Transactional
    public StatusDto updatePayment(PaymentDto paymentDto) {
        Payment payment = paymentRepository
                .findByEmployeeIgnoreCaseAndPeriod(paymentDto.getEmployee(), paymentDto.getPeriod())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record not found"));

        payment.setSalary(paymentDto.getSalary());
        paymentRepository.save(payment);

        return new StatusDto("Updated successfully!");
    }

    public EmployeeDataDto getCurrentEmployeeDataByPeriod(YearMonth period) {
        User currUser = currentUser
                .getCurrentUser()
                .getUserEntity();

        Payment payment = paymentRepository
                .findByEmployeeIgnoreCaseAndPeriod(currUser.getEmail(), period)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record with the specified period not found"));

        return EmployeeDataDto
                .builder()
                .name(currUser.getName())
                .lastname(currUser.getLastName())
                .period(payment.getPeriod())
                .salary(centsToStrDollarsCents(payment.getSalary()))
                .build();
    }

    public List<EmployeeDataDto> getAllCurrentEmployeeData() {
        User currUser = currentUser
                .getCurrentUser()
                .getUserEntity();

        List<Payment> payments = paymentRepository.findAllByEmployeeIgnoreCaseOrderByPeriodDesc(currUser.getEmail());

        return payments
                .stream()
                .map(p -> EmployeeDataDto
                        .builder()
                        .name(currUser.getName())
                        .lastname(currUser.getLastName())
                        .period(p.getPeriod())
                        .salary(centsToStrDollarsCents(p.getSalary()))
                        .build())
                .collect(Collectors.toList());
    }

    private String centsToStrDollarsCents(long cents) {
        return String.format("%d dollar(s) %d cent(s)", cents / 100, cents % 100);
    }
}