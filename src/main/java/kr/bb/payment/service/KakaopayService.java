package kr.bb.payment.service;

import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyResponseDto;
import java.time.LocalDateTime;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class KakaopayService {
  private final PaymentService paymentService;
  private final RestTemplate restTemplate;

  @Value("${kakao.admin}")
  private String ADMIN_KEY;

  @Value("${endpoint.order-service}")
  private String ORDER_SERVICE_URL;

  public KakaopayReadyResponseDto kakaoPayReady(KakaopayReadyRequestDto requestDto) {
    String cid = requestDto.isSubscriptionPay() ? "TCSUBSCRIP" : "TC0ONETIME";

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    parameters.add("cid", cid);
    parameters.add("partner_order_id", requestDto.getOrderId());
    parameters.add("partner_user_id", requestDto.getUserId());
    parameters.add("item_name", requestDto.getItemName());
    parameters.add("quantity", String.valueOf(requestDto.getQuantity()));
    parameters.add("total_amount", String.valueOf(requestDto.getTotalAmount()));
    parameters.add("tax_free_amount", String.valueOf(requestDto.getTaxFreeAMount()));

    parameters.add(
        "approval_url",
            ORDER_SERVICE_URL
            + "/api/orders/approve/"
            + requestDto.getOrderId()
            + "/"
            + requestDto.getOrderType());
    parameters.add("cancel_url", ORDER_SERVICE_URL + "/api/orders/cancel");
    parameters.add("fail_url", ORDER_SERVICE_URL + "/api/orders/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaopayReadyResponseDto responseDto =
        restTemplate.postForObject(url, requestEntity, KakaopayReadyResponseDto.class);

    return responseDto;
  }

  public LocalDateTime kakaoPayApprove(KakaopayApproveRequestDto requestDto) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    parameters.add("cid", requestDto.getCid());
    parameters.add("tid", requestDto.getTid());
    parameters.add("partner_order_id", String.valueOf(requestDto.getOrderId()));
    parameters.add("partner_user_id", String.valueOf(requestDto.getUserId()));
    parameters.add("pg_token", requestDto.getPgToken());

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    String url = "https://kapi.kakao.com/v1/payment/approve";

    KakaopayApproveResponseDto responseDto = restTemplate.postForObject(url, requestEntity, KakaopayApproveResponseDto.class);

    return paymentService.savePaymentInfo(requestDto, responseDto);
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
