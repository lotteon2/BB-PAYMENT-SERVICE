package kr.bb.payment.service;

import bloomingblooms.domain.batch.SubscriptionBatchDto;
import bloomingblooms.domain.batch.SubscriptionBatchDtoList;
import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import kr.bb.payment.dto.request.KakaopayCancelRequestDto;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
import kr.bb.payment.dto.response.KakaopayCancelResponseDto;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.feign.DeliveryServiceClient;
import lombok.RequiredArgsConstructor;
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
  private final DeliveryServiceClient deliveryServiceClient;

  @Value("${kakao.admin}")
  private String ADMIN_KEY;

  @Value("${endpoint.order-service}")
  private String ORDER_SERVICE_URL;

  public KakaopayReadyResponseDto kakaoPayReady(KakaopayReadyRequestDto requestDto) {
    String cid = requestDto.getIsSubscriptionPay() ? "TCSUBSCRIP" : "TC0ONETIME";

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
            + "/approve/"
            + requestDto.getOrderId()
            + "/"
            + requestDto.getOrderType());
    parameters.add("cancel_url", ORDER_SERVICE_URL + "/cancel");
    parameters.add("fail_url", ORDER_SERVICE_URL + "/fail");

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

    return paymentService.saveSinglePaymentInfo(requestDto, responseDto);
  }

  public void renewSubscription(SubscriptionBatchDtoList subscriptionBatchDtoList) {
    Map<Long, Long> oldDeliveryIdsMap = new HashMap<>(); // <결제기록id, old 배송id>

    for(SubscriptionBatchDto subscriptionBatchDto : subscriptionBatchDtoList.getSubscriptionBatchDtoList()){
      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

      parameters.add("cid", subscriptionBatchDto.getCid());
      parameters.add("sid", subscriptionBatchDto.getSid());
      parameters.add("partner_order_id", String.valueOf(subscriptionBatchDto.getPartnerOrderId()));
      parameters.add("partner_user_id", String.valueOf(subscriptionBatchDto.getPartnerUserId()));
      parameters.add("quantity", String.valueOf(subscriptionBatchDto.getQuantity()));
      parameters.add("total_amount", String.valueOf(subscriptionBatchDto.getTotalAmount()));
      parameters.add("tax_free_amount", String.valueOf(0));


      HttpEntity<MultiValueMap<String, String>> requestEntity =
              new HttpEntity<>(parameters, this.getHeaders());

      String url = "https://kapi.kakao.com/v1/payment/subscription";

      KakaopayApproveResponseDto responseDto = restTemplate.postForObject(url, requestEntity, KakaopayApproveResponseDto.class);
      List<Long> subRecordIdAndDeliveryId = paymentService.saveRegularSubscriptionInfo(responseDto);
      oldDeliveryIdsMap.put(subRecordIdAndDeliveryId.get(0), subRecordIdAndDeliveryId.get(1));
    }

    // 배송 생성 및 배송id 저장하기
    List<Long> oldDeliveryIdsList = new ArrayList<>(oldDeliveryIdsMap.values());
    List<Long> newDeliveryIdsList = deliveryServiceClient.createDeliveryForSubscription(oldDeliveryIdsList).getData();
    paymentService.saveDeliveryIds(oldDeliveryIdsMap, newDeliveryIdsList);

  }

  public void cancelPayment(KakaopayCancelRequestDto cancelRequestDto){
    Payment paymentEntity = paymentService.getPaymentEntity(cancelRequestDto.getOrderId());

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    parameters.add("cid", paymentEntity.getPaymentCid());
    parameters.add("tid", paymentEntity.getPaymentTid());
    parameters.add("cancel_amount", String.valueOf(cancelRequestDto.getCancelAmount()));
    parameters.add("cancel_tax_free_amount", String.valueOf(0L));

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

    String url = "https://kapi.kakao.com/v1/payment/cancel";

    restTemplate.postForObject(url, requestEntity, KakaopayCancelResponseDto.class);
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
