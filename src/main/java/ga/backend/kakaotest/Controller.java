package ga.backend.kakaotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private KakaoTalkService kakaoTalkService;

    @PostMapping("/sendBulkKakaoTalkMessage")
    public void sendBulkKakaoTalkMessage(@RequestBody KakaoTalkRequest request) throws JsonProcessingException {
        kakaoTalkService.sendBulkKakaoTalkMessage(request.getUserIds(), request.getMessage(), request.getAccessToken());
    }
}