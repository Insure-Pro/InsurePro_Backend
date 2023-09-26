package ga.backend.photo.repository;

import ga.backend.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByDelYnAndPk(boolean delYn, Long pk);
    List<Photo> findAllByDelYnAndName(boolean delYn, String name);
    List<Photo> findAllByDelYn(boolean delYn);
    List<Photo> findAllByDelYnAndPkAndName(boolean delYn, Long pk, String name);

}
