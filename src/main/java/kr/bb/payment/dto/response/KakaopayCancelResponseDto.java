package kr.bb.payment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaopayCancelResponseDto {
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private ApprovedCancelAmount approved_cancel_amount; // 금번 취소 금액
    private CanceledAmount canceled_amount; // 누적 취소 금액
    private CancelAvailableAmount cancel_available_amount;
    private LocalDateTime created_at;
    private LocalDateTime canceled_at;
}
