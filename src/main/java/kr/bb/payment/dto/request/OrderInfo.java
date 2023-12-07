package kr.bb.payment.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderInfo {
  private String tempOrderId;
  private String itemName;
  private Long quantity;
  private List<OrderInfoByStore> orderInfoByStores;
  private Long sumOfActualAmount;
  private boolean isSubscriptionPay;
  private String ordererName;
  private String ordererPhoneNumber;
  private String ordererEmail;
  private String recipientName;
  private String deliveryZipcode;
  private String deliveryRoadName;
  private String deliveryAddressDetail;
  private String recipientPhone;
  private String deliveryRequest;
}