package ga.backend.address.service;


import ga.backend.address.dto.AddressRequestDto;
import ga.backend.dong.entity.Dong;
import ga.backend.dong.service.DongService;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.service.GuService;
import ga.backend.li.entity.Li;
import ga.backend.li.service.LiService;
import ga.backend.metro.entity.Metro;
import ga.backend.metro.service.MetroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {
    private final MetroService metroService;
    private final GuService guService;
    private final DongService dongService;
    private final LiService liService;

    public void createAddress() {

    }

    public void createAddresses(List<AddressRequestDto.Post> posts) {
        for (AddressRequestDto.Post post:posts) {
            Metro metro = metroService.findMetroByMetroName(post.getMetro());
            if(metro == null) metro = metroService.createMetro(post.getMetro());

            Gu gu = guService.findGuByGuAndMetroPk(post.getGu(), metro.getPk());
            if(gu == null) gu = guService.createGu(post.getGu(), metro);

            Dong dong = dongService.findDongByDongAndGuPk(post.getDong(), gu.getPk());
            if(dong == null) dong = dongService.createDong(post.getDong(), gu);

            Li li = liService.findLiAndGuPk(post.getLi(), dong.getPk());
            if(li == null) {
                Li newLi = new Li();
                newLi.setLiName(post.getLi());
                newLi.setLatitude(post.getLatitude());
                newLi.setLongitude(post.getLongitude());
                newLi.setDong(dong);
                li = liService.createLi(newLi);
            }
        }
    }
}
