package kr.bb.payment.service;

import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaoPayApproveResponseDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.entity.OrderType;
import kr.bb.payment.entity.Payment;
import kr.bb.payment.entity.PaymentStatus;
import kr.bb.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;

  /**
   * 카카오페이 결제 준비 (단건, 정기)
   *
   * @param requestDto
   * @param responseDto
   * @param cid
   * @return responseDto
   */
  @Transactional
  public KakaopayReadyResponseDto savePayReadyInfo(
      KakaopayReadyRequestDto requestDto, KakaopayReadyResponseDto responseDto, String cid) {

    OrderType type =
        (requestDto.getOrderType().equals("ORDER_DELIVERY")
            ? OrderType.ORDER_DELIVERY
            : OrderType.ORDER_PICKUP);

    // Payment 객체를 DB에 저장
    Payment payment =
        Payment.builder()
            .userId(Long.valueOf(requestDto.getUserId()))
            .orderId(Long.valueOf(requestDto.getOrderId()))
            .orderType(type)
            .paymentCid(cid)
            .paymentTid(responseDto.getTid())
            .paymentActualAmount((long) requestDto.getTotalAmount())
            .paymentStatus(PaymentStatus.PENDING)
            .build();
    paymentRepository.save(payment);

    return responseDto;
  }

  /**
   * orderId로 Payment Entity 찾기
   *
   * @param orderId
   * @return
   */
  @Transactional
  public Payment getPaymentEntity(Long orderId) {
    return paymentRepository.findByOrderId(orderId);
  }

  /**
   * 결제수단, 결제상태 업데이트(PENDING -> COMPLETED)
   *
   * @param paymentEntity
   * @param approveResponse
   */
  @Transactional
  public void updatePayInfo(Payment paymentEntity, KakaoPayApproveResponseDto approveResponse) {
    paymentEntity.setPaymentType(approveResponse.getPaymentMethodType());
    paymentEntity.setPaymentStatus(PaymentStatus.COMPLETED);

    paymentRepository.save(paymentEntity);
  }
}
