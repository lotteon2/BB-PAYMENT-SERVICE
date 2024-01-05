package kr.bb.payment.entity;

import bloomingblooms.domain.notification.order.OrderType;
import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kr.bb.payment.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "payment")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long paymentId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "order_id", unique = true, nullable = false)
  private String orderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "orderType", nullable = false)
  private OrderType orderType;

  @Column(name = "payment_cid", nullable = false)
  private String paymentCid;

  @Column(name = "payment_tid", nullable = false)
  private String paymentTid;

  @Column(name = "payment_actual_amount", nullable = false)
  private Long paymentActualAmount;

  @Builder.Default
  @Column(name = "payment_type", nullable = false)
  private String paymentType = "MONEY";

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  private PaymentStatus paymentStatus = PaymentStatus.COMPLETED;

  public static Payment toEntity(KakaopayApproveRequestDto requestDto, OrderType type) {
    return Payment.builder()
        .userId(requestDto.getUserId())
        .orderId(requestDto.getOrderId())
        .orderType(type)
        .paymentCid(requestDto.getCid())
        .paymentTid(requestDto.getTid())
        .paymentActualAmount((long) requestDto.getTotalAmount())
        .paymentStatus(PaymentStatus.PENDING)
        .build();
  }
}
