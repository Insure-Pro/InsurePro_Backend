package ga.backend.address.service;


import ga.backend.dong.service.DongService;
import ga.backend.gu.service.GuService;
import ga.backend.li.service.LiService;
import ga.backend.metro.service.MetroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.statements.SpringRepeat;

@Service
@AllArgsConstructor
public class AddressService {
    private final MetroService metroService;
    private final GuService guService;
    private final DongService dongService;
    private final LiService liService;
}
