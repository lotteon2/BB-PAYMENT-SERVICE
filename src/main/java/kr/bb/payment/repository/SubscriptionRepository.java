package kr.bb.payment.repository;

import kr.bb.payment.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionSid(String subscriptionSid);
}
