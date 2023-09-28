package ga.backend.photo.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.photo.entity.Photo;
import ga.backend.photo.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PhotoService {
    private final PhotoRepository photoRespository;

    // CREATE
    public Photo createPhoto(Photo photo) {
        return photoRespository.save(photo);
    }

    // READ
    public List<Photo> findPhotos(Long pk, String name) {

        List<Photo> photoList;

        if (pk == null && name == null) {
            photoList = photoRespository.findAllByDelYn(false);
        } else if (pk == null) {
            photoList = photoRespository.findAllByDelYnAndName(false, name);
        } else if (name == null) {
            photoList = photoRespository.findAllByDelYnAndPk(false, pk);
        } else {
            photoList = photoRespository.findAllByDelYnAndPkAndName(false, pk, name);
        }
        return photoList;
    }

    public Photo findPhoto(long photoPk) {
        Photo photo = verifiedPhoto(photoPk);
        return photo;
    }

    // UPDATE
    public Photo patchPhoto(Photo photo) {
        Photo findPhoto = verifiedPhoto(photo.getPk());
        Optional.ofNullable(photo.getName()).ifPresent(findPhoto::setName);
        Optional.ofNullable(photo.getPhotoUrl()).ifPresent(findPhoto::setPhotoUrl);

        return photoRespository.save(findPhoto);
    }

    // DELETE
    public Photo deletePhoto(long photoPk) {
        Photo photo = verifiedPhoto(photoPk);
        photo.setDelYn(true);
        photoRespository.save(photo);
        return photo;
    }

    // 검증
    public Photo verifiedPhoto(long photoPk) {
        Optional<Photo> photo = photoRespository.findById(photoPk);
        return photo.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
