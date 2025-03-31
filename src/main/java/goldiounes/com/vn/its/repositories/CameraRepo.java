package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.Camera;
import goldiounes.com.vn.its.models.entitys.Street;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepo extends MongoRepository<Camera, String> {
    List<Camera> findAllByStatusTrue(); // Lấy danh sách camera chưa bị xóa mềm
    Optional<Camera> findByCameraIdAndStatusTrue(String id); // Tìm camera chưa bị xóa mềm theo ID
    long countByStreetAndStatusTrue(Street street); // Đếm số lượng camera chưa bị xóa mềm trong 1 đường
}