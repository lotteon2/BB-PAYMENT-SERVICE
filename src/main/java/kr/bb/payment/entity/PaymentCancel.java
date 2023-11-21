package kr.bb.payment.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kr.bb.payment.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payment_cancel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name ="payment_cancel_id")
    private Long paymentCancelId;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment paymentId;

    @Column(name = "payment_cancel_quantity", unique = true, nullable = false)
    private Long paymentCancelQuantity;

    @Column(name = "payment_cancel_amount", unique = true, nullable = false)
    private Long paymentCancelAmount;

    @Column(name = "payment_cancel_reason", unique = true, nullable = false)
    private Long paymentCancelReason;

    @Column(name = "payment_cancel_status", unique = true, nullable = false)
    private Long paymentCancelStatus;
}

