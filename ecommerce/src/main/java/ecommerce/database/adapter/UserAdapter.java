package ecommerce.database.adapter;

import ecommerce.database.UserRepository;
import ecommerce.database.mapper.UserMapper;
import ecommerce.domain.model.User;
import ecommerce.domain.port.UserPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserAdapter(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public Optional<String> findPasswordHashByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> entity.getPasswordHash());
    }
}
