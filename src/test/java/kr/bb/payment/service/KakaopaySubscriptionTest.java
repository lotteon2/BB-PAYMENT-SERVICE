package kr.bb.payment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bloomingblooms.domain.batch.SubscriptionBatchDto;
import bloomingblooms.domain.batch.SubscriptionBatchDtoList;
import bloomingblooms.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.List;
import kr.bb.payment.dto.response.Amount;
import kr.bb.payment.dto.response.KakaopayApproveResponseDto;
import kr.bb.payment.entity.Subscription;
import kr.bb.payment.entity.SubscriptionRecords;
import kr.bb.payment.feign.DeliveryServiceClient;
import kr.bb.payment.repository.SubscriptionRecordsRepository;
import kr.bb.payment.repository.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
public class KakaopaySubscriptionTest {
  @Autowired private KakaopayService kakaopayService;
  @Autowired private PaymentService paymentService;
  @Autowired private SubscriptionRepository subscriptionRepository;
  @Autowired private SubscriptionRecordsRepository subscriptionRecordsRepository;
  @MockBean private DeliveryServiceClient deliveryServiceClient;
  @Autowired private RestTemplate restTemplate;
  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() throws Exception {
    mockServer = MockRestServiceServer.createServer(restTemplate);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String responseJson = objectMapper.writeValueAsString(createKakaoApproveResponseDto());

    mockServer
        .expect(MockRestRequestMatchers.requestTo("https://kapi.kakao.com/v1/payment/subscription"))
        .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
        .andRespond(MockRestResponseCreators.withSuccess(responseJson, MediaType.APPLICATION_JSON));
  }

  @Test
  @DirtiesContext
  @DisplayName("배치를 통한 구독 결제")
  public void batchSubscription() {
    // given
    SubscriptionBatchDto subscriptionBatchDto = createSubscriptionBatchDto();
    SubscriptionBatchDtoList subscriptionBatchDtoList =
        SubscriptionBatchDtoList.builder()
            .subscriptionBatchDtoList(List.of(subscriptionBatchDto))
            .build();
    SubscriptionRecords subscriptionRecords = createSubscriptionRecords();
    Subscription subscription = createSubscription();
    subscriptionRepository.save(subscription);

    subscriptionRecords.setSubscription(subscription);
    subscriptionRecordsRepository.save(subscriptionRecords);

    CommonResponse<List<Long>> newDeliveryIdsResponse = CommonResponse.success(List.of(2L));
    when(deliveryServiceClient.createDeliveryForSubscription(any()))
        .thenReturn(newDeliveryIdsResponse);

    // when
    kakaopayService.renewSubscription(subscriptionBatchDtoList);

    List<SubscriptionRecords> subscriptionRecordsList = subscription.getSubscriptionRecordsList();
    SubscriptionRecords lastSubscriptionRecords =
        subscriptionRecordsList.get(subscriptionRecordsList.size() - 1);
    Assertions.assertThat(lastSubscriptionRecords.getDeliveryId()).isEqualTo(2L);
  }

  private SubscriptionBatchDto createSubscriptionBatchDto() {
    return SubscriptionBatchDto.builder().userId(1L).orderSubscriptionId("구독 주문 id").build();
  }

  private SubscriptionRecords createSubscriptionRecords() {
    return SubscriptionRecords.builder().deliveryId(1L).subscriptionTotalAmount(44500L).build();
  }

  private Subscription createSubscription() {
    return Subscription.builder()
        .orderSubscriptionId("구독 주문 id")
        .subscriptionCid("TCSUBSCRIP")
        .subscriptionTid("tid 고유번호")
        .subscriptionSid("sid 고유번호")
        .subscriptionQuantity(1L)
        .subscriptionTotalAmount(44500L)
        .paymentDate(LocalDateTime.now())
        .startDate(LocalDateTime.now())
        .userId(1L)
        .phoneNumber("010-1111-1111")
        .build();
  }

  private KakaopayApproveResponseDto createKakaoApproveResponseDto() {
    return KakaopayApproveResponseDto.builder()
        .aid("aid 고유번호")
        .tid("tid 고유번호")
        .cid("TCSUBSCRIP")
        .sid("sid 고유번호")
        .partnerOrderId("주문 id")
        .partnerUserId("1")
        .paymentMethodType("MONEY")
        .itemName("상품명")
        .quantity(1)
        .createdAt(LocalDateTime.now())
        .approvedAt(LocalDateTime.now())
        .amount(new Amount(44500, 0, 0, 0, 0))
        .build();
  }
}
