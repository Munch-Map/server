package skuniv.munchmap.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapService {

    private static final String KAKAO_API_KEY = "908d153e4c63a7f27b53dc0fec2dfdd1";
    private static final String KAKAO_MAP_URL = "https://dapi.kakao.com/v2/maps/staticmap";

    public String getStaticMap(Double latitude, Double longitude) {
        String url = String.format("%s?center=%f,%f&size=1440x1169&markers=color:red|label:U|%f,%f",
                KAKAO_MAP_URL, longitude, latitude, longitude, latitude);

        // API Key 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        // REST API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // 성공 시 결과 반환
        } else {
            throw new RuntimeException("카카오맵 API 호출 실패");
        }
    }
}
