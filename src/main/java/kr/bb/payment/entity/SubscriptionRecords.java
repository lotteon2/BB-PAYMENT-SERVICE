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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
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
  private Subscription subscription;

  @Column(name = "delivery_id", unique = true)
  private Long deliveryId;

  @Column(name = "subscription_total_amount", nullable = false)
  private Long subscriptionTotalAmount;

  public void setSubscription(Subscription subscription){
    this.subscription = subscription;
    subscription.getSubscriptionRecordsList().add(this);
  }

  public void setDeliveryId(Long deliveryId){
    this.deliveryId = deliveryId;
  }
}
