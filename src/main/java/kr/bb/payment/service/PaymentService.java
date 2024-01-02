package kr.bb.payment.service;

import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
import kr.bb.payment.entity.OrderType;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.entity.Subscription;
import kr.bb.payment.entity.SubscriptionRecords;
import kr.bb.payment.mapper.SubscriptionMapper;
import kr.bb.payment.repository.PaymentRepository;
import kr.bb.payment.repository.SubscriptionRecordsRepository;
import kr.bb.payment.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionRecordsRepository subscriptionRecordsRepository;

  /** 카카오페이 결제 준비 (단건, 정기) */
  @Transactional
  public LocalDateTime saveSinglePaymentInfo(
      KakaopayApproveRequestDto requestDto, KakaopayApproveResponseDto responseDto) {

    // 정기 결제 저장
    if (OrderType.ORDER_SUBSCRIPTION.toString().equals(requestDto.getOrderType())) {
      String phoneNumber = requestDto.getPhoneNumber();
      Subscription subscription = SubscriptionMapper.toSubscriptionEntity(phoneNumber, responseDto);
      // 배송id도 함께 저장
      SubscriptionRecords subscriptionRecords =
          SubscriptionMapper.toSubscriptionRecordsEntity(
              responseDto, subscription, requestDto.getDeliveryId());

      // 연관관계 매핑 : 편의 메서드 적용
      subscriptionRecords.setSubscription(subscription);
      subscriptionRepository.save(subscription);
      return subscription.getCreatedAt();
    } else {
      // 단건 결제 저장
      Payment payment = Payment.toEntity(requestDto, OrderType.valueOf(requestDto.getOrderType()));
      paymentRepository.save(payment);
      return payment.getCreatedAt();
    }
  }

  @Transactional
  public List<Long> saveRegularSubscriptionInfo(KakaopayApproveResponseDto responseDto) {
    Subscription subscription = subscriptionRepository.findBySubscriptionSid(responseDto.getSid());
    int lastIdx = subscription.getSubscriptionRecordsList().size() - 1;
    Long oldDeliveryId = subscription.getSubscriptionRecordsList().get(lastIdx).getDeliveryId();

    SubscriptionRecords subscriptionRecords =
        SubscriptionMapper.toSubscriptionRecordsEntity(responseDto, subscription, null);
    subscriptionRepository.save(subscription);
    subscriptionRecords.setSubscription(subscription);
    subscriptionRecordsRepository.save(subscriptionRecords);

    // 가장 마지막 배송id를 반환한다.
    return List.of(subscriptionRecords.getSubscriptionRecordsId(), oldDeliveryId);
  }

  @Transactional
  public void saveDeliveryIds(Map<Long, Long> oldDeliveryIdsMap, List<Long> newDeliveryIdsList) {
    for (int i = 0; i < newDeliveryIdsList.size(); i++) {
      Long key = oldDeliveryIdsMap.keySet().toArray(new Long[0])[i];
      Long value = newDeliveryIdsList.get(i);

      SubscriptionRecords subscriptionRecords =
          subscriptionRecordsRepository.findById(key).orElseThrow(EntityNotFoundException::new);
      subscriptionRecords.setDeliveryId(value);
    }
  }
}
