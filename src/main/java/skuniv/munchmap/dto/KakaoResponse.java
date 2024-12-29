package skuniv.munchmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoResponse {
    @JsonProperty("documents")  // 카카오 api에서 반환되는 데이터 필드 이름 : documents
    private List<KakaoDocument> documents;  // 카카오 api에서 가져온 결과를 나타냄.
}
