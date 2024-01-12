package kr.bb.payment.service;

import bloomingblooms.domain.payment.KakaopayReadyRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import kr.bb.payment.dto.response.Amount;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
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
public class KakaopayReadyTest {
  @Autowired private RestTemplate restTemplate;
  @Autowired private KakaopayService kakaopayService;
  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() throws Exception {
    mockServer = MockRestServiceServer.createServer(restTemplate);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String responseJson = objectMapper.writeValueAsString(kakaopayReadyResponseDto());

    mockServer
            .expect(MockRestRequestMatchers.requestTo("https://kapi.kakao.com/v1/payment/ready"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
  }
  @DisplayName("단건결제 준비 - 픽업")
  @DirtiesContext
  @Test
  void kakaopayReadyForDeliveryAndPickupTest() {
    // given
    KakaopayReadyRequestDto DTO_1 =
        KakaopayReadyRequestDto.builder()
            .userId("1")
            .orderId("1")
            .orderType("PICKUP")
            .itemName("계절마음")
            .quantity(1)
            .totalAmount(52900)
            .taxFreeAMount(0)
            .isSubscriptionPay(false)
            .build();

    // then
    KakaopayReadyResponseDto responseDto = kakaopayService.kakaoPayReady(DTO_1);
    mockServer.verify();
  }

  @DisplayName("단건결제 준비 - 배송&구독")
  @DirtiesContext
  @Test
  void kakaopayReadyForSubscriptionTest() {
    // given
    KakaopayReadyRequestDto DTO_2 =
        KakaopayReadyRequestDto.builder()
            .userId("1")
            .orderId("2")
            .orderType("SUBSCRIBE")
            .itemName("계절마음")
            .quantity(1)
            .totalAmount(52900)
            .taxFreeAMount(0)
            .isSubscriptionPay(true)
            .build();

    // then
    KakaopayReadyResponseDto responseDto2 = kakaopayService.kakaoPayReady(DTO_2);
    mockServer.verify();
  }

  KakaopayApproveResponseDto kakaopayReadyResponseDto() {
    return KakaopayApproveResponseDto.builder()
            .aid("고유번호")
            .tid("tid고유번호")
            .cid("cid번호")
            .sid("sid")
            .partnerOrderId("1")
            .partnerUserId("1")
            .paymentMethodType("MONEY")
            .itemName("상품명")
            .quantity(1)
            .createdAt(LocalDateTime.now())
            .approvedAt(LocalDateTime.now())
            .amount(new Amount(1000, 0, 0, 0, 0))
            .build();
  }
}
