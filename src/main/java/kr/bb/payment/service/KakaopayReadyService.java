package kr.bb.payment.service;

import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
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
public class KakaopayReadyService {
  private final PaymentService paymentService;

  @Value("${kakao.admin}")
  private String ADMIN_KEY;

  @Value("${host.fronturl}")
  private String FRONT_URL;

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
        FRONT_URL + "/payments/approve/" + requestDto.getOrderId() + "/" + requestDto.getUserId());
    parameters.add("cancel_url", FRONT_URL + "/payments/cancel");
    parameters.add("fail_url", FRONT_URL + "/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaopayReadyResponseDto responseDto =
        template.postForObject(url, requestEntity, KakaopayReadyResponseDto.class);

    paymentService.savePayReadyInfo(requestDto, responseDto, cid);

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
