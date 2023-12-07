package kr.bb.payment.service;

import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaoPayApproveResponseDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.entity.Payment;
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

  @Value("${endpoint.apigateway-service}")
  private String APIGATEWAY_SERVICE_URL;

  public KakaopayReadyResponseDto kakaoPayReady(KakaopayReadyRequestDto requestDto) {
    String cid = requestDto.isSubscriptionPay() ? "TC0ONETIME" : "TCSUBSCRIP";

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
        APIGATEWAY_SERVICE_URL
            + "/api/orders/approve/"
            + requestDto.getOrderId()
            + "/"
            + requestDto.getUserId());
    parameters.add("cancel_url", APIGATEWAY_SERVICE_URL + "/api/orders/cancel");
    parameters.add("fail_url", APIGATEWAY_SERVICE_URL + "/api/orders/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaopayReadyResponseDto responseDto =
        restTemplate.postForObject(url, requestEntity, KakaopayReadyResponseDto.class);

    return responseDto;
  }

  public void kakaoPayApprove(KakaopayApproveRequestDto requestDto) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    parameters.add("cid", requestDto.getCid());
    parameters.add("tid", requestDto.getTid());
    parameters.add("partner_order_id", String.valueOf(requestDto.getOrderId()));
    parameters.add("partner_user_id", String.valueOf(requestDto.getUserId()));
    parameters.add("pg_token", requestDto.getPgToken());

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    String url = "https://kapi.kakao.com/v1/payment/approve";

    restTemplate.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

    paymentService.savePaymentInfo(requestDto);
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
