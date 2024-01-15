package kr.bb.payment.service;

import bloomingblooms.domain.notification.order.OrderType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import kr.bb.payment.dto.request.KakaopayCancelRequestDto;
import kr.bb.payment.dto.response.ApprovedCancelAmount;
import kr.bb.payment.dto.response.CancelAvailableAmount;
import kr.bb.payment.dto.response.CanceledAmount;
import kr.bb.payment.dto.response.KakaopayCancelResponseDto;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.entity.PaymentStatus;
import kr.bb.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
public class KakaopayCancelTest {
  @Autowired private RestTemplate restTemplate;
  @Autowired private KakaopayService kakaopayService;
  @Autowired private PaymentRepository paymentRepository;
  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() throws Exception {
    mockServer = MockRestServiceServer.createServer(restTemplate);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String responseJson = objectMapper.writeValueAsString(kakaopayCancelResponseDto());

    mockServer
        .expect(MockRestRequestMatchers.requestTo("https://kapi.kakao.com/v1/payment/cancel"))
        .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
        .andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("카카오 결제 취소 테스트")
  void cancelPay() {
    // given
    KakaopayCancelRequestDto cancelRequestDto =
        KakaopayCancelRequestDto.builder().cancelAmount(2000L).orderId("orderGroupId").build();

    Payment payment = Payment.builder()
            .orderId("orderGroupId")
            .orderType(OrderType.DELIVERY)
            .paymentActualAmount(10000L)
            .paymentCid("TC0ONETIME")
            .paymentStatus(PaymentStatus.PENDING)
            .paymentTid("T59eb9072dff7a6a6515")
            .paymentType("MONEY")
            .userId(1L)
            .build();
    paymentRepository.save(payment);

    // when
    kakaopayService.cancelPayment(cancelRequestDto);

    mockServer.verify();
  }

  private KakaopayCancelResponseDto kakaopayCancelResponseDto() {
    ApprovedCancelAmount approvedCancelAmount =
        ApprovedCancelAmount.builder().total(10000).tax_free(0).vat(0).point(0).build();
    CanceledAmount canceledAmount = CanceledAmount.builder().total(10000).build();
    CancelAvailableAmount cancelAvailableAmount =
        CancelAvailableAmount.builder().total(40000).build();

    return KakaopayCancelResponseDto.builder()
        .cid("cid 번호")
        .status("주문취소")
        .partner_order_id("orderGroupId")
        .partner_user_id("userId")
        .approved_cancel_amount(approvedCancelAmount)
        .canceled_amount(canceledAmount)
        .cancel_available_amount(cancelAvailableAmount)
        .created_at(LocalDateTime.now().minusDays(10))
        .canceled_at(LocalDateTime.now())
        .build();
  }
}
