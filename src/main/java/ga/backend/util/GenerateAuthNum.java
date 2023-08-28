package ga.backend.util;

import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class GenerateAuthNum {
    // 랜덤번호 생성하기
    public int randomNumber() {
        Random random = new Random();
        int num = 0;
        num += (random.nextInt(8)+1) * 1000;
        num += random.nextInt(9) * 100;
        num += random.nextInt(9) * 10;
        num += random.nextInt(9);

        return num; // 1000 ~ 9999
    }

    // 랜덤번호 생성하기
    public int randomNumberExtend() {
        Random r = new Random();
        return r.nextInt(888888) + 111111; // 111111 ~ 999999
    }
}
