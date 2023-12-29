package kr.bb.payment.service;

import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import java.time.LocalDateTime;
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

  /**
   * 카카오페이 결제 준비 (단건, 정기)
   */
  @Transactional
  public LocalDateTime savePaymentInfo(
      KakaopayApproveRequestDto requestDto, KakaopayApproveResponseDto responseDto) {

    // 정기 결제 저장
    if(OrderType.ORDER_SUBSCRIPTION.toString().equals(requestDto.getOrderType())){
      Subscription subscription = SubscriptionMapper.toSubscriptionEntity(responseDto);
      // 배송id도 함께 저장
      SubscriptionRecords subscriptionRecords = SubscriptionMapper.toSubscriptionRecordsEntity(responseDto, subscription, requestDto.getDeliveryId());

      // 연관관계 매핑 : 편의 메서드 적용
      subscriptionRecords.setSubscription(subscription);
      subscriptionRepository.save(subscription);
      return subscription.getCreatedAt();
    } else{
      // 단건 결제 저장
      Payment payment = Payment.toEntity(requestDto, OrderType.valueOf(requestDto.getOrderType()));
      paymentRepository.save(payment);
      return payment.getCreatedAt();
    }
  }
}
