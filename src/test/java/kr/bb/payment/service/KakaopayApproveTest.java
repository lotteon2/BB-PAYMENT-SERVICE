package kr.bb.payment.service;

import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;

import kr.bb.payment.dto.response.Amount;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
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
  @Autowired private RestTemplate restTemplate;
  private MockRestServiceServer mockServer;
  @Autowired private KakaopayService kakaopayService;

  @BeforeEach
  void setUp() throws Exception {
    mockServer = MockRestServiceServer.createServer(restTemplate);

    KakaopayApproveResponseDto responseDto =
        KakaopayApproveResponseDto.builder()
            .aid("A5678901234567890123")
            .tid("T1234567890123456789")
            .cid("TC0ONETIME")
            .sid("sid가상번호")
            .partnerOrderId("partner_order_id")
            .partnerUserId("partner_user_id")
            .paymentMethodType("MONEY")
            .itemName("초코파이")
            .quantity(1)
            .amount(new Amount(2200, 0, 200, 0, 0))
            .createdAt(LocalDateTime.now())
            .approvedAt(LocalDateTime.now())
            .build();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
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

  @DisplayName("결제 승인 테스트 - 배송")
  @DirtiesContext
  @Test
  void kakaoPayApproveForDeliveryTest() {
    KakaopayApproveRequestDto requestDto = creatApproveRequestDto("임시orderId1", "ORDER_DELIVERY");

    kakaopayService.kakaoPayApprove(requestDto);
    mockServer.verify();
  }

  @DisplayName("결제 승인 테스트 - 픽업")
  @DirtiesContext
  @Test
  void kakaoPayApproveForPickupTest() {
    KakaopayApproveRequestDto requestDto = creatApproveRequestDto("임시orderId2", "ORDER_PICKUP");

    kakaopayService.kakaoPayApprove(requestDto);
    mockServer.verify();
  }

  KakaopayApproveRequestDto creatApproveRequestDto(String orderId, String orderType) {
    return KakaopayApproveRequestDto.builder()
        .userId(1L)
        .orderId(orderId)
        .orderType(orderType)
        .itemName("상품명 외 1개")
        .quantity(2)
        .totalAmount(90500)
        .taxFreeAMount(0)
        .isSubscriptionPay(false)
        .cid("가게가맹점코드")
        .tid("T1234567890123456789")
        .pgToken("pg_token=xxxxxxxxxxxxxxxxxxxx")
        .deliveryId(1L)
        .build();
  }

  @DisplayName("결제 승인 테스트 - 구독")
  @DirtiesContext
  @Test
  void kakaoPayApproveForSubscriptionTest() {
    KakaopayApproveRequestDto requestDto =
        creatApproveRequestDto("임시orderId2", "ORDER_SUBSCRIPTION");

    kakaopayService.kakaoPayApprove(requestDto);
    mockServer.verify();
  }
}
