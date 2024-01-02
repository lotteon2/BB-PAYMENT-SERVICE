package kr.bb.payment.feign;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "deliveryServiceClient", url = "${endpoint.delivery-service}")
public interface DeliveryServiceClient {
    @PostMapping(value = "/client/delivery/subscription")
    CommonResponse<List<Long>> createDeliveryForSubscription(List<Long> oldDeliveryIds);
}
