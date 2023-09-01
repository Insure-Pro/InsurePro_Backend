package ga.backend.util;

import ga.backend.gu.service.GuService;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitailGu {
    private final GuService guService;

    static List<String> guList1 = new ArrayList<>(List.of( // 서울특별시, 1
            "종로구", // 1
            "중구", // 2
            "용산구", // 3
            "성동구", // 4
            "광진구", // 5
            "동대문구", // 6
            "중랑구", // 7
            "성북구", // 8
            "강북구", // 9
            "도봉구", // 10
            "노원구", // 11
            "은평구", // 12
            "서대문구", // 13
            "마포구", // 14
            "양천구", // 15
            "강서구", // 16
            "구로구", // 17
            "금천구", // 18
            "영등포구", // 19
            "동작구", // 20
            "관악구", // 21
            "서초구", // 22
            "강남구", // 23
            "송파구", // 24
            "강동구" // 25
    ));

    static List<String> guList2 = new ArrayList<>(List.of( // 부산광역시, 2
            "중구", // 26
            "서구", // 27
            "동구", // 28
            "영도구", // 29
            "부산진구", // 30
            "동래구", // 31
            "남구", // 32
            "북구", // 33
            "강서구", // 34
            "해운대구", // 35
            "사하구", // 36
            "금정구", // 37
            "연제구", // 38
            "수영구", // 39
            "사상구", // 40
            "기장군" // 41
    ));

    InitailGu(GuService guService) {
        this.guService = guService;
        if (this.guService.findGus().isEmpty()) {
//            this.guService.createGus(guList1,  1);
//            this.guService.createGus(guList2,  2);
        }
    }
}
