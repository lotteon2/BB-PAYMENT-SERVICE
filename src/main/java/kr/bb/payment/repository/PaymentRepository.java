package kr.bb.payment.repository;

import java.util.List;
import kr.bb.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(String orderId);
    @Query("select p from Payment p where p.orderId in :orderIds order by p.createdAt desc")
    List<Payment> findAllByOrderIds(List<String> orderIds);
}
