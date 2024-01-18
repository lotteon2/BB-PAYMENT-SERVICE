package kr.bb.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "productServiceClient", url = "${endpoint.product-service")
public interface ProductServiceClient {

}
