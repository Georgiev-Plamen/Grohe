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

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert user roles/authorities to GrantedAuthority (if needed)
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        // Return AppUserDetails (NOT User or UserDetails directly)
        return new AppUserDetails(
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getFirstName(),
                user.getLastName()
        );
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
