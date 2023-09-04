package ga.backend.address.controller;

import ga.backend.address.dto.AddressRequestDto;
import ga.backend.address.service.AddressService;
import ga.backend.dong.mapper.DongMapper;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.mapper.GuMapper;
import ga.backend.li.entity.Li;
import ga.backend.li.mapper.LiMapper;
import ga.backend.metro.mapper.MetroMapper;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl + "/address")
@Validated
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity postAddress(@Valid @RequestBody List<AddressRequestDto.Post> posts) {
        addressService.createAddresses(posts);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
