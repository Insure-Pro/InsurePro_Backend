//package ga.backend.util;
//
//import ga.backend.dong.service.DongService;
//import ga.backend.gu.service.GuService;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static ga.backend.util.InitailGu.guList2;
//
//@Configuration
//public class InitailDong {
//    private final DongService dongService;
//    private final GuService guService;
//
//    List<String> dongList1 = new ArrayList<>(List.of( // 부산광역시, 2 - 중구
//            "중앙동",
//            "동광동",
//            "대청동",
//            "보수동",
//            "부평동",
//            "광복동",
//            "남포동",
//            "영주1동",
//            "영주2동"
//    ));
//
//    List<String> dongList2 = new ArrayList<>(List.of( // 부산광역시, 2 - 서구
//            "동대신1동",
//            "동대신2동",
//            "동대신3동",
//            "서대신1동",
//            "서대신2동",
//            "서대신3동",
//            "서대신4동",
//            "부민동",
//            "아미동",
//            "초장동",
//            "충무동",
//            "남부민1동",
//            "남부민2동",
//            "암남동"
//    ));
//
//    List<String> dongList3 = new ArrayList<>(List.of( // 부산광역시, 2 - 동구
//            "초량1동",
//            "초량2동",
//            "초량3동",
//            "초량6동",
//            "수정1동",
//            "수정2동",
//            "수정3동",
//            "수정4동",
//            "수정5동",
//            "좌천동",
//            "범일1동",
//            "범일2동",
//            "범일5동"
//    ));
//
//    List<String> dongList4 = new ArrayList<>(List.of( // 부산광역시, 2 - 영도구
//            "남항동",
//            "영선1동",
//            "영선2동",
//            "신성동",
//            "봉래1동",
//            "봉래2동",
//            "청학1동",
//            "청학2동",
//            "동삼1동",
//            "동삼2동",
//            "동삼3동"
//    ));
//
//    List<String> dongList5 = new ArrayList<>(List.of( // 부산광역시, 2 - 부산진구
//            "부전1동",
//            "부전2동",
//            "연지동",
//            "초읍동",
//            "양정1동",
//            "양정2동",
//            "전포1동",
//            "전포2동",
//            "부안1동",
//            "부안3동",
//            "당감1동",
//            "당감3동",
//            "당감4동",
//            "가야1동",
//            "가야2동",
//            "개금1동",
//            "개금2동",
//            "개금3동",
//            "범청1동",
//            "범청2동"
//    ));
//
//    List<String> dongList6 = new ArrayList<>(List.of( // 부산광역시, 2 - 동래구
//            "수민동",
//            "복산동",
//            "명륜동",
//            "온천1동",
//            "온천2동",
//            "온천3동",
//            "사직1동",
//            "사직2동",
//            "사직3동",
//            "안락1동",
//            "안락2동",
//            "명장1동",
//            "명장2동"
//    ));
//
//    List<String> dongList7 = new ArrayList<>(List.of( // 부산광역시, 2 - 남구
//            "대연1동",
//            "대연3동",
//            "대연4동",
//            "대연5동",
//            "대연6동",
//            "용호1동",
//            "용호2동",
//            "용호3동",
//            "용호4동",
//            "용당동",
//            "감만1동",
//            "감만2동",
//            "우암동",
//            "문현1동",
//            "문현2동",
//            "문현3동",
//            "문현4동"
//    ));
//
//    List<String> dongList8 = new ArrayList<>(List.of( // 부산광역시, 2 - 북구
//            "구포1동",
//            "구포2동",
//            "구포3동",
//            "금곡동",
//            "화명1동",
//            "화명2동",
//            "화명3동",
//            "덕천1동",
//            "덕천2동",
//            "덕천3동",
//            "만덕1동",
//            "만덕2동",
//            "만덕3동"
//    ));
//
//    List<String> dongList9 = new ArrayList<>(List.of( // 부산광역시, 2 - 강서구
//            "대저1동",
//            "대저2동",
//            "강동동",
//            "명지1동",
//            "명지2동",
//            "가락동",
//            "녹산동",
//            "가덕도동"
//    ));
//
//    List<String> dongList10 = new ArrayList<>(List.of( // 부산광역시, 2 - 해운대구
//            "우1동",
//            "우2동",
//            "우3동",
//            "중1동",
//            "중2동",
//            "좌1동",
//            "좌2동",
//            "좌3동",
//            "좌4동",
//            "송정동",
//            "반여1동",
//            "반여2동",
//            "반여3동",
//            "반여4동",
//            "반송1동",
//            "반송2동",
//            "재송1동",
//            "재송2동"
//    ));
//
//    List<String> dongList11 = new ArrayList<>(List.of( // 부산광역시, 2 - 사하구
//            "괴정1동",
//            "괴정2동",
//            "괴정3동",
//            "괴정4동",
//            "당리동",
//            "하단1동",
//            "하단2동",
//            "신평1동",
//            "신평2동",
//            "장림1동",
//            "장림2동",
//            "다대1동",
//            "다대2동",
//            "구평동",
//            "감천1동",
//            "감천2동"
//    ));
//
//    List<String> dongList12 = new ArrayList<>(List.of( // 부산광역시, 2 - 금정구
//            "서1동",
//            "서2동",
//            "서3동",
//            "금사회동동",
//            "부곡1동",
//            "부곡2동",
//            "부곡3동",
//            "부곡4동",
//            "장전1동",
//            "장전2동",
//            "선두구동",
//            "청룡노포동",
//            "남산동",
//            "구서1동",
//            "구서2동",
//            "금성동"
//    ));
//
//    List<String> dongList13 = new ArrayList<>(List.of( // 부산광역시, 2 - 연제구
//            "거제1동",
//            "거제2동",
//            "거제3동",
//            "거제4동",
//            "연산1동",
//            "연산2동",
//            "연산3동",
//            "연산4동",
//            "연산5동",
//            "연산6동",
//            "연산7동",
//            "연산8동",
//            "연산9동"
//    ));
//
//    List<String> dongList14 = new ArrayList<>(List.of( // 부산광역시, 2 - 수영구
//            "남천1동",
//            "남천2동",
//            "수영동",
//            "망미1동",
//            "망미2동",
//            "광안1동",
//            "광안2동",
//            "광안3동",
//            "광안4동",
//            "민락동"
//    ));
//
//    List<String> dongList15 = new ArrayList<>(List.of( // 부산광역시, 2 - 사상구
//            "삼락동",
//            "모라1동",
//            "모라3동",
//            "덕포1동",
//            "덕포2동",
//            "괘법동",
//            "감천동",
//            "주례1동",
//            "주례2동",
//            "주례3동",
//            "학장동",
//            "엄궁동"
//    ));
//
//    List<String> dongList16 = new ArrayList<>(List.of( // 부산광역시, 2 - 기장군
//            "기장읍",
//            "장안읍",
//            "정관읍",
//            "일광읍",
//            "철마면"
//    ));
//
//
//    InitailDong(DongService dongService,
//                GuService guService) {
//        this.dongService = dongService;
//        this.guService = guService;
//        if (this.dongService.findDongs().isEmpty()) {
//            this.guService.createGus(InitailGu.guList1,  1);
//            this.guService.createGus(InitailGu.guList2,  2);
//
//            this.dongService.createDongs(dongList1, guService.findGuByGuAndMetroPk("중구", 2).getPk());
//            this.dongService.createDongs(dongList2, guService.findGuByGuAndMetroPk("서구", 2).getPk());
//            this.dongService.createDongs(dongList3, guService.findGuByGuAndMetroPk("동구", 2).getPk());
//            this.dongService.createDongs(dongList4, guService.findGuByGuAndMetroPk("영도구", 2).getPk());
//            this.dongService.createDongs(dongList5, guService.findGuByGuAndMetroPk("부산진구", 2).getPk());
//            this.dongService.createDongs(dongList6, guService.findGuByGuAndMetroPk("동래구", 2).getPk());
//            this.dongService.createDongs(dongList7, guService.findGuByGuAndMetroPk("남구", 2).getPk());
//            this.dongService.createDongs(dongList8, guService.findGuByGuAndMetroPk("북구", 2).getPk());
//            this.dongService.createDongs(dongList9, guService.findGuByGuAndMetroPk("강서구", 2).getPk());
//            this.dongService.createDongs(dongList10, guService.findGuByGuAndMetroPk("해운대구", 2).getPk());
//            this.dongService.createDongs(dongList11, guService.findGuByGuAndMetroPk("사하구", 2).getPk());
//            this.dongService.createDongs(dongList12, guService.findGuByGuAndMetroPk("금정구", 2).getPk());
//            this.dongService.createDongs(dongList13, guService.findGuByGuAndMetroPk("연제구", 2).getPk());
//            this.dongService.createDongs(dongList14, guService.findGuByGuAndMetroPk("수영구", 2).getPk());
//            this.dongService.createDongs(dongList15, guService.findGuByGuAndMetroPk("사상구", 2).getPk());
//            this.dongService.createDongs(dongList16, guService.findGuByGuAndMetroPk("기장군", 2).getPk());
//        }
//    }
//}
