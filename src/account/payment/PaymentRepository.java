package account.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, BigInteger> {
    boolean existsByEmployeeIgnoreCaseAndPeriod(String employee, YearMonth period);

    Optional<Payment> findByEmployeeIgnoreCaseAndPeriod(String employee, YearMonth period);

    List<Payment> findAllByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);
}