package ga.backend.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.filter.Filter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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

        // 모든 ERROR 로그를 파일에 기록하도록 설정
        if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            writeSecurityLogToFile();

            String message = event.getMessage();
            if (event.getThrowableProxy() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(message).append(System.lineSeparator());

                // 예외 스택 트레이스 추가
                for (StackTraceElementProxy line : event.getThrowableProxy().getStackTraceElementProxyArray()) {
                    sb.append("   ").append(line).append(System.lineSeparator());
                    if (sb.length() > 1000) {
                        sb.append("... [truncated]");
                        break;
                    }
                }

                message = sb.toString();
            }

            // 파일에 로그 메시지를 기록
            writeLogToFile(formatLogMessage(event, message));
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

    // 로그 메시지 포맷팅
    private String formatLogMessage(ILoggingEvent event, String message) {
        // 로그 타임스탬프
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(event.getTimeStamp()));

        // 로그 레벨
        String level = event.getLevel().toString();

        // 스레드 이름
        String threadName = event.getThreadName();

        // 로거 이름
        String loggerName = event.getLoggerName();

        // 포맷된 로그 메시지
        return String.format("%s  %-5s --- [%s] %s : %s", timestamp, level, threadName, loggerName, message);
    }

    // 파일명 설정
    private String getFormattedFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));  // 시간대를 Asia/Seoul로 설정
        String dateTime = sdf.format(new Date());
        return String.format(fileNamePattern, dateTime);
    }

    // Spring Security Debugger 출력  -> Requset 내용 추가
    private void writeSecurityLogToFile() {
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
    }
}
