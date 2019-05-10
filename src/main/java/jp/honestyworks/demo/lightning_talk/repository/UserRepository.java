package jp.honestyworks.demo.lightning_talk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.honestyworks.demo.lightning_talk.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOptionalByEmail(String userEmail);
	

}
