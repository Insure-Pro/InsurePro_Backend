package ga.backend.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthChecker {
    @GetMapping("/health-check")
    public ResponseEntity postCompany() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
