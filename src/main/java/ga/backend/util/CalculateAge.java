package ga.backend.util;

import java.time.LocalDate;
import java.time.Period;

// 만나이 구하기
public class CalculateAge {
    static public int getAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();

        // 생년월일과 현재 날짜를 비교하여 나이 계산
        return Period.between(birthDate, currentDate).getYears();
    }
}
