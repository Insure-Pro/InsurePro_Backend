package ga.backend.li.service;

import ga.backend.dong.service.DongService;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.li.entity.Li;
import ga.backend.li.repository.LiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LiService {
    private final LiRepository liRespository;
    private final DongService dongService;

    // CREATE
    public Li createLi(Li li, long dongPk) {
        li.setDong(dongService.verifiedDong(dongPk));
        return liRespository.save(li);
    }

    public Li createLi(String liName, long dongPk) {
        Li li = new Li();
        li.setDong(dongService.verifiedDong(dongPk));
        li.setLi(liName);
        return liRespository.save(li);
    }

    public void createLis(List<String> liNames, long dongPk) {
        liNames.forEach(liName -> createLi(liName, dongPk));
    }

    // READ
    public Li findLi(long liPk) {
        Li li = verifiedLi(liPk);
        return li;
    }

    // 모든 Li 반환
    public List<Li> findLis() {
        List<Li> lis = liRespository.findAll();
        return lis;
    }

    // dong-pk에 해당하는 Li 반환
    public List<Li> findLis(long dongPk) {
        List<Li> lis = liRespository.findByDong_Pk(dongPk);
        return lis;
    }

    // li이름과 gu-pi로 Li 반환
    public Li findLiAndGuPk(String liName, long dongPk) {
        Optional<Li> li = liRespository.findByLiAndDong_Pk(liName, dongPk);
        return li.orElse(null);
    }

    // li 이름으로 Li 반환
    public Li findLiByLi(String liName) {
        Optional<Li> li = liRespository.findByLi(liName);
        return li.orElse(null);
    }

    // UPDATE
    public Li patchLi(Li li) {
        Li findLi = verifiedLi(li.getPk());
        Optional.ofNullable(li.getLi()).ifPresent(findLi::setLi);
        Optional.ofNullable(li.getDelYn()).ifPresent(findLi::setDelYn);
        if(li.getLatitude() != 0.0) findLi.setLatitude(li.getLatitude());
        if(li.getLongitude() != 0.0) findLi.setLongitude(li.getLongitude());
        return liRespository.save(findLi);
    }

    // DELETE
    public void deleteLi(long liPk) {
        Li li = verifiedLi(liPk);
        liRespository.delete(li);
    }

    // 검증
    public Li verifiedLi(long liPk) {
        Optional<Li> li = liRespository.findById(liPk);
        return li.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}