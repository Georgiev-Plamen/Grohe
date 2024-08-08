package bg.deplan.Grohe.service;

import bg.deplan.Grohe.data.UserRepository;
import bg.deplan.Grohe.model.AppUserDetails;
import bg.deplan.Grohe.model.User;
import bg.deplan.Grohe.model.Role;
import bg.deplan.Grohe.model.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(AppUserDetailsService::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    private static UserDetails map(User user) {

        return new AppUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(Role::getName).map(AppUserDetailsService::map).toList(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private static GrantedAuthority map(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
