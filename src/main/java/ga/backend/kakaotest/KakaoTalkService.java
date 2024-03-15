package ga.backend.kakaotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.backend.kakaotest.Dto.KakaoMessage;
import ga.backend.kakaotest.Dto.Link;
import ga.backend.kakaotest.Dto.TemplateObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KakaoTalkService { // 카톡 친구 보내기

    //    @Value("${kakao.api.key}")
//    private String kakaoApiKey;
    private static final String KAKAO_API_URL = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";

    public void sendBulkKakaoTalkMessage(String[] userIds, String message, String accessToken) throws JsonProcessingException {
//        String kakaoTalkApiUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        List<String> failedUsers = new ArrayList<>();

        for (String userId : userIds) {
//            try {
                // 전송 메세지
//                Link link = new Link("", "");
//                TemplateObject templateObject = new TemplateObject("text", message, link, "button");
//                KakaoMessage requestBody = new KakaoMessage(List.of(userIds), templateObject);

                // 요청 본문 객체를 JSON으로 변환
//                ObjectMapper objectMapper = new ObjectMapper();
//                String requestBodyJson = objectMapper.writeValueAsString(requestBody);
//                System.out.println("!! request : " + requestBodyJson);

                // HTTP Body 생성
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//                map.add("receiver_uuids", String.join(",", userIds));
                map.add("receiver_uuids", "[\"" + userId + "\"]");
                map.add("template_object", "{\"object_type\": \"text\", \"text\": \"" + message + "\", \"link\": { \"web_url\": \"\",\"mobile_web_url\": \"\"}}");

                // HTTP 요청 엔티티 생성
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);


                // HTTP 요청
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        KAKAO_API_URL,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                // 예외처리
                if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                    failedUsers.add(userId);
                }
//            } catch (Exception e) {
//                // 예외 처리
//                failedUsers.add(userId);
//            }
        }

        // 전송 실패한 사람들
        if (!failedUsers.isEmpty()) {
            System.out.println("Following recipients failed to receive the message: " + failedUsers);
        }
    }
}