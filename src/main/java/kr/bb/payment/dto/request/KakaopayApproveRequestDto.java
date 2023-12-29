package kr.bb.payment.dto.request;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
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
  @NotNull private Long userId;
  @NotNull private String orderId;
  @NotNull private String orderType;
  @NotNull private String itemName;
  @NotNull private int quantity;
  @NotNull private int totalAmount;
  @NotNull private int taxFreeAMount;
  @NotNull private boolean isSubscriptionPay;
  @NotNull private String cid;
  @NotNull private String tid;
  @NotNull private String pgToken;
  @Nullable private Long deliveryId;
}
