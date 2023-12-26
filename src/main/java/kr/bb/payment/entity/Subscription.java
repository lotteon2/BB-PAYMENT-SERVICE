package kr.bb.payment.entity;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kr.bb.payment.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscription_id")
  private Long subscriptionId;

  @Column(name = "order_subscription_id", unique = true, nullable = false)
  private String orderSubscriptionId;

  @Column(name = "subscription_cid", nullable = false)
  private String subscriptionCid;

  @Column(name = "subscription_tid", unique = true, nullable = false)
  private String subscriptionTid;

  @Column(name = "subscription_sid", unique = true, nullable = false)
  private String subscriptionSid;

  @Column(name = "subscription_quantity", nullable = false)
  private Long subscriptionQuantity;

  @Column(name = "subscription_total_amount", nullable = false)
  private Long subscriptionTotalAmount;

  @Column(name = "payment_date", nullable = false)
  private LocalDate paymentDate;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;
}
