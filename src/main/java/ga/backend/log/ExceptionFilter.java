package ga.backend.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.filter.Filter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class ExceptionFilter extends Filter<ILoggingEvent> {
    private ILoggingEvent securityLogEvent;
    private final String fileNamePattern = "logs/errors/errors-%s.log"; // 파일명 패턴 설정

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String logger = event.getLoggerName();

        // "Spring Security Debugger" 로그를 저장
        if (logger.contains("Spring Security Debugger")) {
            securityLogEvent = event;
        }

        // BusinessLogicException일 경우 -> Requset 내용 추가
        if (logger.contains("BusinessLogicException")) {
            // 저장된 "Spring Security Debugger" 로그 출력
            if (securityLogEvent != null) {
                String message = securityLogEvent.getMessage();
                message = message.split("Security filter chain")[0];
                message += "************************************************************";

                // 파일에 로그 메시지를 기록
                writeLogToFile(message);

                // securityLogEvent를 null로 설정해 중복 기록 방지
                securityLogEvent = null;
            }

            return FilterReply.ACCEPT;
        }

        // GlobalExceptionHandler일 경우 -> 내용 요약하기
        if (logger.contains("GlobalExceptionHandler")) {
            // 전체 메시지 가져오기
            String message = event.getMessage();

            // Throwable이 있는 경우, 스택 트레이스를 가져와서 메시지 추가
            if (event.getThrowableProxy() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(message).append(System.lineSeparator());

                // 예외 스택 트레이스 추가
                for (StackTraceElementProxy line : event.getThrowableProxy().getStackTraceElementProxyArray()) {
                    sb.append(line).append(System.lineSeparator());
                    // 메시지 길이를 제한하여 생략 처리
                    if (sb.length() > 1000) {
                        sb.append("... [truncated]");
                        break;
                    }
                }

                message = sb.toString();
            }

            // 파일에 로그 메시지를 기록
            writeLogToFile(message);
        }

        return FilterReply.DENY;
    }

    // 파일에 로그 남기기
    private void writeLogToFile(String logMessage) {
        String formattedFileName = getFormattedFileName();
        try (FileWriter fileWriter = new FileWriter(formattedFileName, true)) {
            fileWriter.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }

    // 파일명 설정
    private String getFormattedFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));  // 시간대를 Asia/Seoul로 설정
        String dateTime = sdf.format(new Date());
        return String.format(fileNamePattern, dateTime);
    }
}
