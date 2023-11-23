package kr.bb.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaopayReadyResponseDto {
    private String tid;
    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;
}
