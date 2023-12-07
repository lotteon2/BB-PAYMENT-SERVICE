package kr.bb.payment.feign;

import bloomingblooms.response.SuccessResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productServiceClient", url = "${endpoint.product-service")
public interface ProductServiceClient {

  // 유효한지
  @PostMapping("/api/products/prices")
  SuccessResponse<Map<Long, Long>> getPricesByProductIds(
      @RequestParam("productIds") List<Long> productIds);
}
