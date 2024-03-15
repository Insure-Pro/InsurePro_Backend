package ga.backend.kakaotest.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KakaoMessage {
    private List<String> receiver_uuids;
    private TemplateObject template_object;

    public KakaoMessage(List<String> receiverUuids, TemplateObject templateObject) {
        this.receiver_uuids = receiverUuids;
        this.template_object = templateObject;
    }
}