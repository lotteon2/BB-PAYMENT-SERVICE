package kr.bb.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PaymentControllerTest.class)
public class PaymentControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
//  @MockBean private PaymentService paymentService;

  // given
  KakaopayReadyRequestDto DTO_1 =
      KakaopayReadyRequestDto.builder()
          .userId("1")
          .orderId("1")
          .orderType("ORDER_DELIVERY")
          .itemName("계절마음")
          .quantity(1)
          .totalAmount(52900)
          .taxFreeAMount(0)
          .isSubscriptionPay(false)
          .build();

  @Test
  public void kakaopayReady() throws Exception {
    mockMvc
        .perform(
            post("/ready")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(DTO_1)))
        .andExpect(status().isOk());
  }
}
