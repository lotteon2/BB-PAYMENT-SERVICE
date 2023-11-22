package kr.bb.payment.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaopayReadyRequestDto {
    private String userId;
    private String orderId;
    private String orderType;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAMount;
    private boolean isSubscriptionPay;
}
