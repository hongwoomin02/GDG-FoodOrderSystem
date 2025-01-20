package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
@DisplayName("UserRepository 테스트")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 저장하기 테스트")
    public void testSaveUser() {
        // given
        User user = new User("h@.com", "h", "h");
        // when
        User savedUser = userRepository.save(user);
        // then
        assertNotNull(savedUser.getId());
        assertThat(savedUser.getId()).isNotNull();
    }
    @Test
    @DisplayName("Id로 회원 검색")
    void testFindById() {
        // given
        User user = new User("h@.com", "h", "h");
        User savedUser = userRepository.save(user);
        // when
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("모든 회원 조회")
    void testFindAll() {
        // given
        User user1 = new User("h1@.com", "h", "h");
        User user2 = new User("h2@.com", "h", "h");

        userRepository.save(user1);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).isNotNull();
        assertThat(users.get(0).getEmail()).isEqualTo("h1@.com");
        assertThat(users.get(1).getEmail()).isEqualTo("h2@.com");
    }

    @Test
    @DisplayName("회원삭제")
    void testDelete() {
        // given
        User user = new User("h@.com", "h", "h");
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        // then
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }


}
