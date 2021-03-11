package ph.apper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ph.apper.domain.User;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    Stream<User> findAllByIsVerifiedAndIsActive(boolean isVerified, boolean isActive);
}
