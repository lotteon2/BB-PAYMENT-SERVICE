package kr.bb.payment.service;

import java.time.LocalDateTime;
import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
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
   * @return void
   */
  @Transactional
  public LocalDateTime savePaymentInfo(
      KakaopayApproveRequestDto requestDto) {

    OrderType type =
        (requestDto.getOrderType().equals("ORDER_DELIVERY")
            ? OrderType.ORDER_DELIVERY
            : OrderType.ORDER_PICKUP);

    // Payment 객체를 DB에 저장
    Payment payment = Payment.toEntity(requestDto, type);
    paymentRepository.save(payment);
    return payment.getCreatedAt();
  }
}
