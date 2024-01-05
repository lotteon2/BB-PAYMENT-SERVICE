package kr.bb.payment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.payment.PaymentInfoDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PaymentServiceTest {
    @Autowired private PaymentService paymentService;
    @Test
    void getPaymentInfoTest(){
        List<PaymentInfoDto> paymentInfo = paymentService.getPaymentInfo(List.of("임시주문번호"));
        assertThat(paymentInfo.size()).isEqualTo(0);
    }

    @Test
    void getPaymentDateTest(){
        String paymentDate = paymentService.getPaymentDate("임시주문번호");
        assertThat(paymentDate).isEmpty();
    }
}
