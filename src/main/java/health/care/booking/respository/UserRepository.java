package health.care.booking.respository;

import health.care.booking.models.Role;
import health.care.booking.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String userId);

    User findByRoles(Role role);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);




}

