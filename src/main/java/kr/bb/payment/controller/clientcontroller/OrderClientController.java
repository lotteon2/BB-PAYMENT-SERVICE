package kr.bb.payment.controller.clientcontroller;

import kr.bb.payment.dto.request.KakaopayReadyRequestDto;
import kr.bb.payment.dto.response.KakaopayReadyResponseDto;
import kr.bb.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderClientController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<KakaopayReadyResponseDto> payReady(
            @RequestBody KakaopayReadyRequestDto readyRequestDto) {
        paymentService.payReady(readyRequestDto);
    }
}
