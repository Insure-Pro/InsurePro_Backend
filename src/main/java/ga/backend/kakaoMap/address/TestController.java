package ga.backend.kakaoMap.address;

import ga.backend.util.FindCoordinateByKakaoMap;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping(Version.currentUrl + "/test")
@Validated
@AllArgsConstructor
public class TestController {
    private final FindCoordinateByKakaoMap testAddress;

    @PostMapping
    public Map<String, Double> getCustomer(@RequestBody TestDto.test test) throws UnsupportedEncodingException {
        return testAddress.findCoordinate(test.getAddress());
//    public JSONObject getCustomer(@RequestParam("address") String address) throws UnsupportedEncodingException {
//        return testAddress.address(address);
    }

}
