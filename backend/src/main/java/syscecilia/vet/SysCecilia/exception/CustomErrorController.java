package syscecilia.vet.SysCecilia.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ProblemDetail> handleError(HttpServletRequest request) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exceptionObj = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object requestURI = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        int status = statusObj != null ? (int) statusObj : 500;
        HttpStatus httpStatus = HttpStatus.valueOf(status);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                httpStatus,
                getDetailMessage(exceptionObj, status)
        );
        
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setType(URI.create("https://syscecilia.vet/problems/" + httpStatus.value()));
        problemDetail.setProperty("timestamp", Instant.now());
        
        if (requestURI != null) {
            problemDetail.setInstance(URI.create(requestURI.toString()));
        }
        
        // Add field errors if it's a deserialization error
        if (status == 400 && exceptionObj instanceof Exception) {
            addFieldErrorsIfApplicable(problemDetail, (Exception) exceptionObj);
        }
        
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }
    
    private String getDetailMessage(Object exceptionObj, int status) {
        if (exceptionObj instanceof Exception) {
            Exception ex = (Exception) exceptionObj;
            
            if (ex instanceof com.fasterxml.jackson.core.JsonParseException) {
                return "Invalid JSON format: " + ex.getMessage();
            }
            
            Throwable cause = ex.getCause();
            if (cause instanceof JsonMappingException) {
                JsonMappingException jsonEx = (JsonMappingException) cause;
                if (jsonEx instanceof InvalidFormatException) {
                    InvalidFormatException ife = (InvalidFormatException) jsonEx;
                    return String.format("Invalid type for field '%s': %s", 
                            ife.getPath().stream()
                                    .map(JsonMappingException.Reference::getFieldName)
                                    .collect(Collectors.joining(".")),
                            ife.getValue()
                    );
                }
                return jsonEx.getOriginalMessage();
            }
            
            return ex.getMessage() != null ? ex.getMessage() : HttpStatus.valueOf(status).getReasonPhrase();
        }
        
        return HttpStatus.valueOf(status).getReasonPhrase();
    }
    
    private void addFieldErrorsIfApplicable(ProblemDetail problemDetail, Exception exception) {
        Map<String, String> errors = new HashMap<>();
        Throwable current = exception;
        
        while (current != null) {
            if (current instanceof JsonMappingException) {
                JsonMappingException jsonEx = (JsonMappingException) current;
                if (jsonEx instanceof InvalidFormatException) {
                    InvalidFormatException ife = (InvalidFormatException) jsonEx;
                    String fieldName = ife.getPath().stream()
                            .map(JsonMappingException.Reference::getFieldName)
                            .collect(Collectors.joining("."));
                    
                    if (!fieldName.isEmpty()) {
                        String errorMsg = String.format(
                                "Invalid value for field '%s': received '%s', expected type '%s'",
                                fieldName,
                                ife.getValue(),
                                ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown"
                        );
                        errors.put(fieldName, errorMsg);
                    }
                }
            }
            current = current.getCause();
        }
        
        if (!errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }
    }
}

