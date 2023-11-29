package ga.backend.kakaoMap.address;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class TestAddress {
    public JSONObject address(String query) throws UnsupportedEncodingException {
        // kakaoMap 연결 URL
//        String addr =
//                "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";
//        addr = addr + "x=" + 126.99599512792346 + "&y=" + 35.976749396987046;
        String addr = "https://dapi.kakao.com/v2/local/search/address.json?";
        String encode = URLEncoder.encode(query, "UTF-8");
        addr += "query=" + encode;

        try {
            //위의 주소를 가지고 URL 객체를 생성
            URL url = new URL(addr);

            //URL객체를 가지고 HttpURLConnection 객체 만들기
            var con = (HttpURLConnection) url.openConnection();

            //인증받기
            con.setRequestProperty("Authorization", "KakaoAK e426558528f8e9954d1490f3995d95c9");

            //옵션을 설정
            con.setConnectTimeout(20000);
            con.setUseCaches(false);

            //줄 단위 데이터 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            //문자열을 임시로 저장할 객체 만들기
            StringBuilder sb = new StringBuilder();
            while (true) {
                //한 줄의 데이터 읽기
                String line = br.readLine();
//                System.out.println(line);
                //읽은 데이터가 없으면 반복문 종료
                if (line == null) {
                    break;
                }
                //읽은 데이터가 있으면 sb에 추가
                sb.append(line);
            }

            //연결 해제
            br.close();
            con.disconnect();

            //데이터 출력
//            System.out.println("!! sb : " + sb);
            //JSONObject를 생성
            JSONObject obj = new JSONObject(sb.toString());
//            System.out.println("!! obj : " + obj);

            JSONArray imsi = obj.getJSONArray("documents");
//            System.out.println("!! imsi : " + imsi);

            JSONObject o = imsi.getJSONObject(0);
            String address = o.getString("address_name");
            String x = o.getString("x");
            String y = o.getString("y");
//            System.out.println("!! address : " + address);
            System.out.println("!! x : " + x);
            System.out.println("!! y : " + y);

            return obj;
        } catch (Exception e) {
            System.out.println("주소 가져오기 실패:" + e.getMessage());
        }
        return null;
    }
}