package kr.bb.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoPayApproveResponseDto {
  private String aid; // 요청 고유번호
  private String tid; // 결제 고유번호
  private String cid; // 가맹점 코드
  private String sid; // 정기 결제용 id
  @JsonProperty("partner_order_id")
  private String partnerOrderId; // 가맹점 주문번호
  @JsonProperty("partner_user_id")
  private String partnerUserId; // 가맹점 회원
  @JsonProperty("payment_method_type")
  private String paymentMethodType; // 결제수단
  @JsonProperty("item_name")
  private String itemName;
  private Integer quantity;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("approved_at")
  private String approvedAt;
  private Amount amount;
}
