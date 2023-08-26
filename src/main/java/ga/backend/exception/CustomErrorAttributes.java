package ga.backend.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
//import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final String ERROR_INTERNAL_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        storeErrorAttributes(request, ex);
        System.out.println("!! exception : " + ex.getMessage());
//        System.out.println("!! " + ex.);
//        response.setStatus();
        return null;
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_INTERNAL_ATTRIBUTE, ex);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//        System.out.println("!! parameterNames : ");
//        while(webRequest.getParameterNames().hasNext()) {
//            System.out.println("!! parameterNames : " + webRequest.getParameterNames().next());
//        }
//        while(webRequest.getHeaderNames().hasNext()) {
//            System.out.println("!! "  + webRequest.getHeaderNames().next());
//        }
//        System.out.println("!! : " + webRequest.getContextPath());
//        System.out.println("!! " + webRequest.getLocale().toString());
//        System.out.println("!! " + Arrays.toString(webRequest.getParameterMap().keySet().toArray()));
//
//        Throwable error = getError(webRequest);
//        System.out.println("!! error : " + error.getMessage());
//        System.out.println("!! error : " + error.getCause().getMessage());
//        System.out.println("!! error : " + error.getLocalizedMessage());
//        System.out.println("!! error : " + error.toString());
//        error.printStackTrace();
//        for(Throwable throwable : error.getSuppressed()) {
//            System.out.println("*".repeat(50));
//            System.out.println("!! error2 : " + throwable.getMessage());
//        }
//        for(StackTraceElement stackTraceElement: error.getStackTrace()) {
//            System.out.println("*".repeat(50));
//            System.out.println("!! error3 : " + stackTraceElement.toString());
//            System.out.println("!! error3 : " + stackTraceElement.getClassLoaderName());
//            System.out.println("!! error3 : " + stackTraceElement.getClassName());
//            System.out.println("!! error3 : " + stackTraceElement.getFileName());
//            System.out.println("!! error3 : " + stackTraceElement.getModuleName());
//            System.out.println("!! error3 : " + stackTraceElement.getMethodName());
//            System.out.println("!! error3 : " + stackTraceElement.getMethodName());
//            System.out.println("!! error3 : " + stackTraceElement.getLineNumber());
//            System.out.println("!! error3 : " + stackTraceElement.getModuleVersion());
//        }


//        ----------------------------------------------------------------------------------
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);
        result.remove("timestamp");
        if(result.get("exception").toString().contains("BusinessLogicException")) {
            ExceptionCode exceptionCode = ExceptionCode.findByMessage(result.get("message").toString());
            if(exceptionCode != null) {
                result.put("status", exceptionCode.getStatus());
            }
        }



//        result.put("greeting", "Hello");
//        System.out.println("!! options key : " + Arrays.toString(options.getIncludes().toArray()));
//        System.out.println("!! error key : " + Arrays.toString(result.keySet().toArray()));
        return result;
    }

    public int getStatus() {

    }
}