package kr.bb.payment.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaopayApproveRequestDto {
    private Long orderId;
    private Long userId;
    private String orderType; // 배송/픽업 판별용
    private String tid;
    private String pgToken;
}
