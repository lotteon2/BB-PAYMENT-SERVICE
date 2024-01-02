package kr.bb.payment.kafka;

import bloomingblooms.domain.batch.SubscriptionBatchDto;
import java.util.List;
import kr.bb.payment.service.KakaopayService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer<T> {
  private final KakaopayService kakaopayService;

  @KafkaListener(topics = "subscription-batch", groupId = "payment-subscription")
  public void subscriptionBatch(List<SubscriptionBatchDto> subscriptionBatchDtoList) {
    kakaopayService.renewSubscription(subscriptionBatchDtoList);
  }
}
