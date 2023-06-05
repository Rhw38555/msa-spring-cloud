package com.example.userservice.jpa;

import com.example.userservice.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("USER JPA TEST")
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("save user test")
    void givenUser_whenSave_thenWorksFine() {
        // Given
        UserEntity user = new UserEntity();
        user.setUserId("testUserId1");
        user.setName("testName");
        user.setEmail("testEmail@google.com");
        user.setEncryptedPwd("sfdsafsdfsdafadsfsd+asdf");

        // when
        UserEntity newUserEntity = userRepository.save(user);

        // then
        assertThat(newUserEntity.getUserId())
                .isNotNull()
                .isEqualTo(user.getUserId());

        assertThat(newUserEntity.getEmail())
                .isNotNull()
                .isEqualTo(user.getEmail());


    }

    @Test
    @DisplayName("findByUserId")
    void givenUser_whenSelectingByUserId_thenWorksFine() {
        //Given
        UserEntity user = new UserEntity();
        user.setUserId("testUserId1");
        user.setName("testName");
        user.setEmail("testEmail@google.com");
        user.setEncryptedPwd("sfdsafsdfsdafadsfsd+asdf");

        // When
        userRepository.save(user);
        String tmpUserId = "testUserId1";
        UserEntity userEntity = userRepository.findByUserId(tmpUserId);

        // Then
        assertThat(userEntity.getUserId())
                .isNotNull()
                .isEqualTo(user.getUserId());

    }

    @Test
    void givenUser_whenSelectByEmail_thenWorksFine() {
        //Given
        UserEntity user = new UserEntity();
        user.setUserId("testUserId1");
        user.setName("testName");
        user.setEmail("testEmail@google.com");
        user.setEncryptedPwd("sfdsafsdfsdafadsfsd+asdf");

        // When
        userRepository.save(user);
        String tmpEmail = "testEmail@google.com";
        UserEntity userEntity = userRepository.findByUserId(tmpEmail);

        // Then
        assertThat(userEntity.getEmail())
                .isNotNull()
                .isEqualTo(user.getEmail());
    }
}