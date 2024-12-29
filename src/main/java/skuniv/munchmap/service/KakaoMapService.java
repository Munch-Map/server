package skuniv.munchmap.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import skuniv.munchmap.dto.KakaoDocument;
import skuniv.munchmap.dto.KakaoResponse;

import java.util.List;
import java.util.Map;

@Service
public class KakaoMapService {
    @Value("${spring.kakao.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public KakaoMapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 주소로 가게 이름 찾기
    public Map<String, Object> searchByAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }

    // 가게 이름으로 주소 찾기
    public List<KakaoDocument> searchByPlace(String name) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + name;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // KakaoResponse로 매핑
        ResponseEntity<KakaoResponse> response = restTemplate.exchange(
                url,    // 요청을 보낼 URL
                HttpMethod.GET,
                entity,     // HttpEntity 객체
                KakaoResponse.class
        );

        // documents 가져오기
        KakaoResponse kakaoResponse = response.getBody();
        if (kakaoResponse == null || kakaoResponse.getDocuments().isEmpty()) {
            throw new RuntimeException("No results found");
        }

        return kakaoResponse.getDocuments();
    }
}
