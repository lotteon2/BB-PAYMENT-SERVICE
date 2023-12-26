package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.response.CommonResponse;
import java.time.LocalDateTime;
import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.service.KakaopayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class OrderClientController {

  private final KakaopayService kakaopayService;

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
}
