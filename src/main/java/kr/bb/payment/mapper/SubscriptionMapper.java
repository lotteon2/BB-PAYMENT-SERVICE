package kr.bb.payment.mapper;

import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
import kr.bb.payment.entity.Subscription;
import kr.bb.payment.entity.SubscriptionRecords;

public class SubscriptionMapper {
  public static Subscription toSubscriptionEntity(
      String phoneNumber, KakaopayApproveResponseDto responseDto) {
    return Subscription.builder()
        .orderSubscriptionId(responseDto.getPartnerOrderId())
        .subscriptionCid(responseDto.getCid())
        .subscriptionTid(responseDto.getTid())
        .subscriptionSid(responseDto.getSid())
        .subscriptionQuantity(Long.valueOf(responseDto.getQuantity()))
        .subscriptionTotalAmount(Long.valueOf(responseDto.getAmount().getTotal()))
        .startDate(responseDto.getCreatedAt()) // 시작일
        .paymentDate(responseDto.getCreatedAt().plusDays(30)) // 다음 결제일을 저장
        .userId(Long.valueOf(responseDto.getPartnerUserId()))
        .phoneNumber(phoneNumber)
        .build();
  }

  public static SubscriptionRecords toSubscriptionRecordsEntity(
      KakaopayApproveResponseDto responseDto, Subscription subscription, Long deliveryId) {
    return SubscriptionRecords.builder()
        .subscription(subscription)
        .deliveryId(deliveryId)
        .subscriptionTotalAmount(Long.valueOf(responseDto.getAmount().getTotal()))
        .build();
  }
}
