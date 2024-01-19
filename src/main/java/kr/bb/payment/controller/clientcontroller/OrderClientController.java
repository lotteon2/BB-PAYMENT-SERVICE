package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.domain.batch.SubscriptionBatchDtoList;
import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyResponseDto;
import bloomingblooms.domain.payment.PaymentInfoDto;
import bloomingblooms.response.CommonResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kr.bb.payment.dto.request.KakaopayCancelRequestDto;
import kr.bb.payment.service.KakaopayService;
import kr.bb.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class OrderClientController {

  private final KakaopayService kakaopayService;
  private final PaymentService paymentService;

  @PostMapping("/ready")
  public CommonResponse<KakaopayReadyResponseDto> payReady(
      @RequestBody KakaopayReadyRequestDto readyRequestDto) {

    return CommonResponse.success(kakaopayService.kakaoPayReady(readyRequestDto));
  }

  @PostMapping("/approve")
  public CommonResponse<LocalDateTime> payApprove(
      @RequestBody KakaopayApproveRequestDto approveRequestDto) {
    return CommonResponse.success(kakaopayService.kakaoPayApprove(approveRequestDto));
  }

  @PostMapping("/paymentInfo")
  CommonResponse<Map<String, PaymentInfoDto>> getPaymentInfo(@RequestBody List<String> orderGroupIds){
    return CommonResponse.success(paymentService.getPaymentInfo(orderGroupIds));
  }

  @GetMapping(value = "/paymentDate")
  CommonResponse<String> getPaymentDate(@RequestParam String orderGroupId){
    return CommonResponse.success(paymentService.getPaymentDate(orderGroupId));
  }

  @PostMapping(value = "/cancel")
  CommonResponse<Void> cancel(@RequestBody KakaopayCancelRequestDto cancelRequestDto){
    kakaopayService.cancelPayment(cancelRequestDto);
    return CommonResponse.success(null);
  }

  @PostMapping(value = "/subscription")
  CommonResponse<Void> subscription(@RequestBody SubscriptionBatchDtoList subscriptionBatchDtolist){
    kakaopayService.renewSubscription(subscriptionBatchDtolist);
    return CommonResponse.success(null);
  }

  @PostMapping(value = "/subscription/cancel")
  CommonResponse<Void> cancelSubscription(@RequestBody KakaopayCancelRequestDto cancelRequestDto){
    kakaopayService.cancelSubscription(cancelRequestDto);
    return CommonResponse.success(null);
  }
}
