package kr.bb.payment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kr.bb.payment.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription_records")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionRecords extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscription_records_id")
  private Long subscriptionRecordsId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subcription_id")
  private Subscription subscriptionId;

  @Column(name = "delivery_id", unique = true, nullable = false)
  private Long deliveryId;

  @Column(name = "subscription_total_amount", unique = true, nullable = false)
  private Long subscriptionTotalAmount;

  @Column(name = "subscription_status", unique = true, nullable = false)
  private String subscriptionStatus;
}
