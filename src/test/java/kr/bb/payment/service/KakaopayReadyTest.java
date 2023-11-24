package kr.bb.payment.service;

import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class KakaopayReadyTest {
  @Autowired private KakaopayService kakaopayService;

  @DisplayName("단건결제 준비 - 픽업")
  @DirtiesContext
  @Test
  void kakaopayReadyForDeliveryAndPickupTest() {
    // given
    KakaopayReadyRequestDto DTO_1 =
        KakaopayReadyRequestDto.builder()
            .userId("1")
            .orderId("1")
            .orderType("ORDER_PICKUP")
            .itemName("계절마음")
            .quantity(1)
            .totalAmount(52900)
            .taxFreeAMount(0)
            .isSubscriptionPay(false)
            .build();

    // then
    KakaopayReadyResponseDto responseDto = kakaopayService.kakaoPayReady(DTO_1);
    Assertions.assertEquals(20, responseDto.getTid().length());
    Assertions.assertTrue(responseDto.getNextRedirectPcUrl().startsWith("https://"));
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
            .orderType("ORDER_DELIVERY")
            .itemName("계절마음")
            .quantity(1)
            .totalAmount(52900)
            .taxFreeAMount(0)
            .isSubscriptionPay(true)
            .build();

    // then
    KakaopayReadyResponseDto responseDto2 = kakaopayService.kakaoPayReady(DTO_2);
    Assertions.assertEquals(20, responseDto2.getTid().length());
    Assertions.assertTrue(responseDto2.getNextRedirectPcUrl().startsWith("https://"));
  }
}
