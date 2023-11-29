package ga.backend.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class FindCoordinateByKakaoMap {
    @Value("${kakao.key}")
    private String KAKAO_KEY;

    public Map<String, Double> findCoordinate(String query) throws UnsupportedEncodingException {
        // kakaoMap 연결 URL
        String addr = "https://dapi.kakao.com/v2/local/search/address.json?";
        String encode = URLEncoder.encode(query, "UTF-8");
        addr += "query=" + encode;

        try {
            //위의 주소를 가지고 URL 객체를 생성
            URL url = new URL(addr);

            //URL객체를 가지고 HttpURLConnection 객체 만들기
            var con = (HttpURLConnection) url.openConnection();

            //인증받기
            con.setRequestProperty("Authorization", KAKAO_KEY);

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

                //읽은 데이터가 없으면 반복문 종료
                if (line == null) {
                    break;
                }
                //읽은 데이터가 있으면 sb에 추가
                sb.append(line);
            }

            // 연결 해제
            br.close();
            con.disconnect();

            // JSONObject 생성
            JSONObject obj = new JSONObject(sb.toString());
            JSONObject o = obj.getJSONArray("documents").getJSONObject(0);

            // 좌표값 구하기
            Double x = Double.valueOf(o.getString("x"));
            Double y = Double.valueOf(o.getString("y"));

            Map<String, Double> coordinate = new HashMap<>();
            coordinate.put("x", x);
            coordinate.put("y", y);

            return coordinate;
        } catch (Exception e) {
            System.out.println("주소 가져오기 실패:" + e.getMessage());
        }

        return null;
    }
}