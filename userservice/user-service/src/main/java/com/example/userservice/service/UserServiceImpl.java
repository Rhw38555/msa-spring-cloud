package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.security.JwtAuthTokenProvider;
import com.example.userservice.vo.JwtToken;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    JwtAuthTokenProvider jwtAuthTokenProvider;
    AuthenticationManagerBuilder authenticationManagerBuilder;
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory,
                           AuthenticationManagerBuilder authenticationManagerBuilder,
                           JwtAuthTokenProvider jwtAuthTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
        this.circuitBreakerFactory =  circuitBreakerFactory;
        this.authenticationManagerBuilder =  authenticationManagerBuilder;
        this.jwtAuthTokenProvider =  jwtAuthTokenProvider;
    }

    @Override
    public JwtToken login(String email, String password){
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken token = jwtAuthTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public UserDto getUserDetailByEmail(String email){
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
    // 시큐리티의 매니저에서 인증 시 user 인증 조건

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(userName);
        if(userEntity == null)
            throw new UsernameNotFoundException(userName);
        // user, pwd, 계정 익스파이어 시킬거냐 ..
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());


        ModelMapper mapper = new ModelMapper();
        // 변환 전략, 정확히 일치하는 필드만 매핑
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        //UserDto를 UserEntity 객체로 변환
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // 다른 오더 서비스에서 데이터 전달 받음, RestTemplate
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET,
//                null, new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        // 다른 오더 서비스에서 데이터 전달 받음, feign client
//        List<ResponseOrder> orderList = null;
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//        }catch(FeignException ex){
//            log.error(ex.getMessage());
//        }

        // ErrorDecoder 적용
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);

        // circuitbreaker
        log.info("Before call orders microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>()); // 반환값을 어떻게 처리할것인지
        log.info("After call orders microservice");
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }
}
