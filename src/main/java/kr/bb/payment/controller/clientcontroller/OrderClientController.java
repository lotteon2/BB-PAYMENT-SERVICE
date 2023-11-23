package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.response.SuccessResponse;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.service.KakaopayReadyService;
import kr.bb.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderClientController {

  private final PaymentService paymentService;
  private final KakaopayReadyService kakaopayReadyService;

  @PostMapping("/ready")
  public ResponseEntity<SuccessResponse<KakaopayReadyResponseDto>> payReady(
      @RequestBody KakaopayReadyRequestDto readyRequestDto) {

    KakaopayReadyResponseDto responseDto = kakaopayReadyService.kakaoPayReady(readyRequestDto);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<KakaopayReadyResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .data(responseDto)
                .build());
  }
}
