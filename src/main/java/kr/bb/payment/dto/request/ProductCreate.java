package kr.bb.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreate {
  private Long productId;
  private String productName;
  private Long quantity;
  private Long price;
}
