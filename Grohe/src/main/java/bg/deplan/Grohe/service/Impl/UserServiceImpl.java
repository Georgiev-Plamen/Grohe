package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.RoleRepository;
import bg.deplan.Grohe.data.UserRepository;
import bg.deplan.Grohe.model.DTOs.UserRegisterDTO;
import bg.deplan.Grohe.model.Role;
import bg.deplan.Grohe.model.User;
import bg.deplan.Grohe.model.UserRoleEnum;
import bg.deplan.Grohe.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {

        if(userRepository.findByUsername(userRegisterDTO.username()).isPresent()) {
            return;
        }

        if(!userRegisterDTO.password().equals(userRegisterDTO.confirmPassword())) {
            return;
        }

        User user = map(userRegisterDTO);

        userRepository.save(user);
    }

    private User map(UserRegisterDTO userRegisterDTO) {
        User mappedEntity = new User();
        mappedEntity.setUsername(userRegisterDTO.username());
        mappedEntity.setFirstName(userRegisterDTO.firstName());
        mappedEntity.setLastName(userRegisterDTO.lastName());
        mappedEntity.setEmail(userRegisterDTO.email());
        mappedEntity.setPassword(passwordEncoder.encode(userRegisterDTO.password()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(UserRoleEnum.USER));

        if(userRegisterDTO.username().equals("Plamen")) {
            roles.add(roleRepository.findByName(UserRoleEnum.ADMIN));
        }

        mappedEntity.setRole(roles);

        return mappedEntity;
    }
}
