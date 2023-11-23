package kr.bb.payment.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

  /** JpaRepository 사용 */
  @Test
  @DisplayName("단건결제 정보 저장 테스트")
  void savePayment() {
    // given
    // when
    // then
  }
}
