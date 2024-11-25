//package skuniv.munchmap.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.*;
//import java.util.List;
//
//@Data
//public class KakaoAddressResponseDTO {
//
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Schema(title = "카카오지도api 요청")
//    public class searchAddress {
//        private List<Document> documents;
//
//        @Data
//        public static class Document {
//            private String address_name;
//            private RoadAddress road_address;
//
//            @Data
//            public static class RoadAddress {
//                private String address_name;
//                private String region_1depth_name;
//                private String region_2depth_name;
//                private String region_3depth_name;
//                private String road_name;
//                private String x;
//                private String y;
//            }
//        }
//    }
//
//
//}
