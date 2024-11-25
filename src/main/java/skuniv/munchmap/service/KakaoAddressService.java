//package skuniv.munchmap.service;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestClientException;
//import skuniv.munchmap.config.exception.BadRequestException;
//import skuniv.munchmap.config.exception.ErrorResponseStatus;
//import skuniv.munchmap.dto.KakaoAddressResponseDTO;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoAddressService {
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Value("${kakao.api.key}")
//    private String apiKey;
//
//    public KakaoAddressResponseDTO.searchAddress searchAddress(String query) {
//        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
//                .queryParam("query", query)
//                .build()
//                .toUriString();
//
//        // 헤더에 Authorization 추가
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + apiKey);
//
//        try {
//            ResponseEntity<KakaoAddressResponseDTO.searchAddress> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    new HttpEntity<>(headers),
//                    KakaoAddressResponseDTO.searchAddress.class
//            );
//
//            return Optional.ofNullable(response.getBody())
//                    .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.NOT_EXIST_ADDRESS));
//        } catch (RestClientException e) {
//            throw new BadRequestException(ErrorResponseStatus.INVALID_KAKAO_ADDRESS);
//        }
//    }
//}
