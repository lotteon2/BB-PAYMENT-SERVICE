package kr.bb.payment.entity;

public enum PaymentStatus {
  PENDING("주문 중"),
  COMPLETED("주문 완료"),
  CANCELED("주문 취소");

  private final String message;

  PaymentStatus(String message) {
    this.message = message;
  }
}
