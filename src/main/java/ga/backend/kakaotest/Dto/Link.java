package ga.backend.kakaotest.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class Link {
    private String web_url;
    private String mobile_web_url;

    public Link(String web_url, String mobile_web_url) {
        this.web_url = web_url;
        this.mobile_web_url = mobile_web_url;
    }
}
