package kr.bb.payment.controller.clientcontroller;

import bloomingblooms.response.SuccessResponse;
import kr.bb.payment.dto.request.KakaopayApproveRequestDto;
import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.service.KakaopayService;
import kr.bb.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class OrderClientController {

  private final PaymentService paymentService;
  private final KakaopayService kakaopayService;

  @PostMapping("/ready")
  public ResponseEntity<SuccessResponse<KakaopayReadyResponseDto>> payReady(
      @RequestBody KakaopayReadyRequestDto readyRequestDto) {

    KakaopayReadyResponseDto responseDto = kakaopayService.kakaoPayReady(readyRequestDto);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<KakaopayReadyResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .data(responseDto)
                .build());
  }

  @PostMapping("/approve")
  public ResponseEntity<SuccessResponse<Void>> payApprove(@RequestBody KakaopayApproveRequestDto approveRequestDto){

    kakaopayService.kakaoPayApprove(approveRequestDto);

    return ResponseEntity.ok().body(SuccessResponse.<Void>builder()
            .code(String.valueOf(HttpStatus.OK.value()))
            .message(HttpStatus.OK.name())
            .build());
  }
}
