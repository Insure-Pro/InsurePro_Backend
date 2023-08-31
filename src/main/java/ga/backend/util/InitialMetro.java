package ga.backend.util;

import ga.backend.metro.service.MetroService;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitialMetro {
    private final MetroService metroService;

    List<String> metroList = new ArrayList<>(List.of(
            "서울특별시", // 1
            "부산광역시", // 2
            "대구광역시", // 3
            "인천광역시", // 4
            "광주광역시", // 5
            "대전광역시", // 6
            "울산광역시", // 7
            "세종특별자치시", // 8
            "경기도", // 9
            "강원특별자치도", // 10
            "충청북도", // 11
            "충청남도", // 12
            "전라북도", // 13
            "전라남도", // 14
            "경상북도", // 15
            "경상남도", // 16
            "제주특별자치도" // 17
    ));

    InitialMetro(MetroService metroService) {
        this.metroService = metroService;
        if(this.metroService.findMetros().isEmpty()) {
            metroList.forEach(metroService::createMetro);
        }
    }


}
