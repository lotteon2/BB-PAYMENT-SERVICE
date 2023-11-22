package kr.bb.payment.service;

import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.entity.OrderType;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.entity.PaymentStatus;
import kr.bb.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;

  @Value("${kakao.admin}")
  private String ADMIN_KEY;

  @Value("${host.fronturl}")
  private String FRONT_URL;

  /**
   * 카카오페이 결제 준비 (단건, 정기)
   *
   * @param dto
   * @return
   */
  public KakaopayReadyResponseDto payReady(KakaopayReadyRequestDto dto) {
    String cid = dto.isSubscriptionPay() ? "TC0ONETIME" : "TCSUBSCRIP";

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    parameters.add("cid", cid);
    parameters.add("partner_order_id", dto.getOrderId());
    parameters.add("partner_user_id", dto.getUserId());
    parameters.add("item_name", dto.getItemName());
    parameters.add("quantity", String.valueOf(dto.getQuantity()));
    parameters.add("total_amount", String.valueOf(dto.getTotalAmount()));
    parameters.add("tax_free_amount", String.valueOf(dto.getTaxFreeAMount()));

    parameters.add(
        "approval_url", FRONT_URL + "/payments/approve/" + dto.getOrderId() + "/" + dto.getUserId());
    parameters.add("cancel_url", FRONT_URL + "/payments/cancel");
    parameters.add("fail_url", FRONT_URL + "/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaopayReadyResponseDto responseDto =
        template.postForObject(url, requestEntity, KakaopayReadyResponseDto.class);

    // Payment 객체를 DB에 저장
    OrderType type= (dto.getOrderType().equals("ORDER_DELIVERY") ? OrderType.ORDER_DELIVERY: OrderType.ORDER_PICKUP);
    Payment payment = Payment.builder()
            .userId(Long.valueOf(dto.getUserId()))
            .orderId(Long.valueOf(dto.getOrderId()))
            .orderType(type)
            .paymentCid(cid)
            .paymentTid(responseDto.getTid())
            .paymentActualAmount((long) dto.getTotalAmount())
            .paymentStatus(PaymentStatus.PENDING)
            .build();
    paymentRepository.save(payment);

    return responseDto;
  }

  @NotNull
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();

    String auth = "KakaoAK " + ADMIN_KEY;
    headers.set("Authorization", auth);
    headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    return headers;
  }
}
