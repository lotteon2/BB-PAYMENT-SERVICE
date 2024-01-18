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
public class KakaopayCancelSubscriptionResponseDto {
  private String status;
  private LocalDateTime created_at;
  private LocalDateTime inactivated_at;
  private LocalDateTime last_approved_at;
}
