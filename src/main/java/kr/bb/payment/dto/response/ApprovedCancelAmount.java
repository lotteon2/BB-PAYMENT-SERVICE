package kr.bb.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedCancelAmount {  // 이번 요청으로 취소된 금액
    private Integer total;
    private Integer tax_free;
    private Integer vat;
    private Integer point;
}
