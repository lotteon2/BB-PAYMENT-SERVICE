package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.response.SuccessResponse;
import java.time.LocalDateTime;
import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.service.KakaopayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public SuccessResponse<KakaopayReadyResponseDto> payReady(
      @RequestBody KakaopayReadyRequestDto readyRequestDto) {

    KakaopayReadyResponseDto responseDto = kakaopayService.kakaoPayReady(readyRequestDto);

    return SuccessResponse.<KakaopayReadyResponseDto>builder()
        .data(responseDto)
        .message(HttpStatus.OK.name())
        .build();
  }

  @PostMapping("/approve")
  public SuccessResponse<LocalDateTime> payApprove(
      @RequestBody KakaopayApproveRequestDto approveRequestDto) {

    LocalDateTime paymentDateTime = kakaopayService.kakaoPayApprove(approveRequestDto);

    return SuccessResponse.<LocalDateTime>builder()
        .data(paymentDateTime)
        .message(HttpStatus.OK.name())
        .build();
  }
}
