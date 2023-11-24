package kr.bb.payment.dto.response;

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
@NoArgsConstructor
public class Amount {

  private Integer total;
  private Integer tax_free;
  private Integer vat;
  private Integer point;
  private Integer discount;

  public Amount(Integer total, Integer tax_free, Integer vat, Integer point, Integer discount) {
    this.total = total;
    this.tax_free = tax_free;
    this.vat = vat;
    this.point = point;
    this.discount = discount;
  }
}
