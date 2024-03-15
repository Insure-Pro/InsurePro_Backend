package ga.backend.kakaotest.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateObject {
    private String object_type;
    private String text;
    private Link link;
    private String button_title;

    public TemplateObject(String object_type, String text, Link link, String button_title) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
        this.button_title = button_title;
    }
}
