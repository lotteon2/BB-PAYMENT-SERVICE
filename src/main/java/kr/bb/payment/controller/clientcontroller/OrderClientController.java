package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.domain.payment.KakaopayApproveRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyRequestDto;
import bloomingblooms.domain.payment.KakaopayReadyResponseDto;
import bloomingblooms.domain.payment.PaymentInfoDto;
import bloomingblooms.response.CommonResponse;
import java.time.LocalDateTime;
import java.util.List;
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

  @GetMapping("/paymentInfo")
  CommonResponse<List<PaymentInfoDto>> getPaymentInfo(@RequestParam List<String> orderGroupIds){
    return CommonResponse.success(paymentService.getPaymentInfo(orderGroupIds));
  }

  @GetMapping(value = "/client/paymentDate")
  CommonResponse<String> getPaymentDate(@RequestParam String orderGroupId){
    return CommonResponse.success(paymentService.getPaymentDate(orderGroupId));
  }
}
