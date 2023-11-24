package kr.bb.payment.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
import kr.bb.payment.dto.response.Amount;
import kr.bb.payment.dto.response.KakaoPayApproveResponseDto;
import kr.bb.payment.entity.OrderType;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.entity.PaymentStatus;
import kr.bb.payment.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
public class KakaopayApproveTest {
  @Autowired
  private RestTemplate restTemplate;
  private MockRestServiceServer mockServer;
  @Autowired
  private KakaopayService kakaopayService;
  @Autowired
  private PaymentRepository paymentRepository;

  @BeforeEach
  void setUp() throws Exception{
    mockServer = MockRestServiceServer.createServer(restTemplate);

    // Payment Entity 저장
    Payment payment =
            Payment.builder()
                    .userId(1L)
                    .orderId(1L)
                    .orderType(OrderType.ORDER_DELIVERY)
                    .paymentCid("TC0ONETIME")
                    .paymentTid("FAKE_TID_FOR_TEST")
                    .paymentActualAmount(52900L)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();
    paymentRepository.save(payment);

    KakaoPayApproveResponseDto responseDto =
            KakaoPayApproveResponseDto.builder()
                    .aid("A5678901234567890123")
                    .tid("T1234567890123456789")
                    .cid("TC0ONETIME")
                    .partnerOrderId("partner_order_id")
                    .partnerUserId("partner_user_id")
                    .paymentMethodType("MONEY")
                    .itemName("초코파이")
                    .quantity(1)
                    .amount(new Amount(2200, 0, 200, 0, 0))
                    .createdAt("2016-11-15T21:18:22")
                    .approvedAt("2016-11-15T21:20:47")
                    .build();

    ObjectMapper objectMapper = new ObjectMapper();
    String responseJson = objectMapper.writeValueAsString(responseDto);

    mockServer
            .expect(MockRestRequestMatchers.requestTo("https://kapi.kakao.com/v1/payment/approve"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
  }

  @AfterEach
  void shutDown() {
    mockServer.reset();
  }

  @DisplayName("결제 승인 테스트")
  @DirtiesContext
  @Test
  void kakaoPayApproveTest() {
    KakaopayApproveRequestDto requestDto =
            KakaopayApproveRequestDto.builder()
                    .userId(1L)
                    .orderId(1L)
                    .tid("T1234567890123456789")
                    .pgToken("pg_token=xxxxxxxxxxxxxxxxxxxx")
                    .build();

    kakaopayService.kakaoPayApprove(requestDto);

    mockServer.verify();
  }
}
