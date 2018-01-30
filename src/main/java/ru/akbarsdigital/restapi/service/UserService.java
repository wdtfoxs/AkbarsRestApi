package ru.akbarsdigital.restapi.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.akbarsdigital.restapi.configurations.root.security.model.UserDetailsImpl;
import ru.akbarsdigital.restapi.configurations.root.security.util.JwtTokenUtils;
import ru.akbarsdigital.restapi.entity.User;
import ru.akbarsdigital.restapi.exception.AuthenticationException;
import ru.akbarsdigital.restapi.exception.ConfirmationException;
import ru.akbarsdigital.restapi.exception.EditException;
import ru.akbarsdigital.restapi.exception.RestException;
import ru.akbarsdigital.restapi.repository.UserRepository;
import ru.akbarsdigital.restapi.util.EmailValidation;
import ru.akbarsdigital.restapi.util.PhoneValidation;
import ru.akbarsdigital.restapi.web.dto.ConfirmDto;
import ru.akbarsdigital.restapi.web.dto.LoginDto;
import ru.akbarsdigital.restapi.web.dto.ProfileDto;
import ru.akbarsdigital.restapi.web.dto.RegistrationDto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Log4j2
@Service
public class UserService implements UserDetailsService {
    private static final String FILE_PATTERN = "^[A-z0-9]+\\.(jpg|png)$";

    @Value("${log.auth}")
    private boolean logAuth;
    @Value("${log.registration}")
    private boolean logReg;
    @Value("${log.confirm}")
    private boolean logConfirm;
    @Value("${log.edit}")
    private boolean logEdit;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtTokenUtils token;

    @Transactional
    public String authentication(LoginDto dto) {
        if (logAuth)
            log.info("Trying to auth a user with data: " + dto);
        User user = getUser(dto.getEmail());
        if (!user.isConfirmed())
            throw new AuthenticationException("User not confirmed");
        if (!encoder.matches(dto.getPassword(), user.getPassword()))
            throw new AuthenticationException("Password was incorrect");
        if (logAuth) {
            log.info("Trying to auth with data: " + dto);
        }
        String userToken = this.token.generateToken(user.getId(), user.getEmail(), user.getLastPasswordChange());
        if (logAuth)
            log.info("Success authentication user with data: " + dto + ". Token for user: " + userToken);
        return userToken;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void registrationNewUser(RegistrationDto user) {
        if (logReg)
            log.info("Trying to register a user with data " + user);
        if (logReg)
            log.info("Success register a user with data: " + user);
        userRepository.save(User.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(encoder.encode(user.getPassword()))
                .confirmedCode("123qwerty")
                .lastPasswordChange(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void confirmAccount(ConfirmDto user) {
        if (logConfirm)
            log.info("Attempt to confirm the account with data " + user);
        Optional<User> userOptional = userRepository.findByPhone(user.getPhone());
        if (!userOptional.isPresent())
            throw new ConfirmationException("User not found");
        if (userOptional.get().isConfirmed())
            throw new ConfirmationException("User already confirmed");
        if (!user.getCode().equals(userOptional.get().getConfirmedCode()))
            throw new ConfirmationException("Wrong code");
        userRepository.confirmUser(user.getPhone());
        if (logConfirm)
            log.info("User with phone " + user.getPhone() + " confirmed");
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent())
            throw new RestException("User not found");
        return userOptional.get();
    }

    @Transactional
    public void editProfile(ProfileDto profile, UserDetailsImpl user) {
        if (logEdit)
            log.info("Trying to edit user with id: " + user.getId());
        if (profile == null || (profile.getName() == null && profile.getSurname() == null && profile.getPatronymic() == null &&
                profile.getPhone() == null && profile.getEmail() == null && profile.getAvatar() == null && profile.getPassword() == null))
            throw new EditException("Empty data");
        User db = getUser(user.getEmail());
        if (profile.getEmail() != null && !db.getEmail().equals(profile.getEmail())) {
            if (!EmailValidation.validateEmail(profile.getEmail()))
                throw new EditException("Wrong email format");
            if (userRepository.existsByEmail(profile.getEmail()))
                throw new EditException("User with this email already exists");
            db.setEmail(profile.getEmail());
        }
        if (profile.getPhone() != null && !db.getPhone().equals(profile.getPhone())) {
            if (!PhoneValidation.validatePhone(profile.getPhone()))
                throw new EditException("Invalid phone format");
            if (userRepository.existsByPhone(profile.getPhone()))
                throw new EditException("User with this phone already exists");
            db.setPhone(profile.getPhone());
        }
        if (profile.getPassword() != null && !encoder.matches(profile.getPassword(), db.getPassword())) {
            db.setPassword(encoder.encode(profile.getPassword()));
            db.setLastPasswordChange(LocalDateTime.now());
        }
        if (profile.getAvatar() != null && Pattern.compile(FILE_PATTERN).matcher(profile.getAvatar()).matches())
            db.setAvatar(profile.getAvatar());
        if (profile.getName() != null)
            db.setName(profile.getName());
        if (profile.getSurname() != null)
            db.setSurname(profile.getSurname());
        if (profile.getPatronymic() != null)
            db.setPatronymic(profile.getPatronymic());

        userRepository.save(db);
        if (logEdit)
            log.info("User with id " + user.getId() + " successfully edited");
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent())
            throw new UsernameNotFoundException(email);

        return new UserDetailsImpl(user.get().getId(), user.get().getEmail(), user.get().getLastPasswordChange());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public LocalDateTime lastPasswordChange(Long id){
        return userRepository.lastPasswordChange(id);
    }
}
