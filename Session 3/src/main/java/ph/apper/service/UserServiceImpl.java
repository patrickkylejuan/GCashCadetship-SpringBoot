package ph.apper.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.domain.User;
import ph.apper.domain.VerificationCode;
import ph.apper.exception.InvalidLoginCredentialException;
import ph.apper.exception.InvalidUserRegistrationRequestException;
import ph.apper.exception.InvalidVerificationRequestException;
import ph.apper.exception.UserNotFoundException;
import ph.apper.payload.UpdateUserRequest;
import ph.apper.payload.UserData;
import ph.apper.payload.UserRegistrationRequest;
import ph.apper.payload.UserRegistrationResponse;
import ph.apper.repository.UserRepository;
import ph.apper.repository.VerificationCodeRepository;
import ph.apper.util.IdService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Profile({"dev","prod"})
@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public UserServiceImpl(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws InvalidUserRegistrationRequestException {
        LocalDate parsedBirthDate = LocalDate.parse(request.getBirthDate());
        UserServiceUtil.validateUserAge(parsedBirthDate);

        if (isRegisteredAndVerifiedUser(request.getEmail())) {
            throw new InvalidUserRegistrationRequestException("email already registered");
        }

        // get user id
        String userId = IdService.getNextUserId();

        LOGGER.info("Generated User ID: {}", userId);
        // save registration details as User with ID
        User newUser = new User(userId);
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setBirthDate(parsedBirthDate);
        newUser.setEmail(request.getEmail());
        newUser.setPassword(BCrypt.withDefaults().hashToString(4, request.getPassword().toCharArray()));
        newUser.setDateRegistered(LocalDateTime.now());

        String code = IdService.generateCode(6);
        LOGGER.info("Generated verification code: {}", code);

        VerificationCode verificationCode = new VerificationCode(request.getEmail(), code);

        userRepository.save(newUser);
        verificationCodeRepository.save(verificationCode);

        return new UserRegistrationResponse(code);
    }

    @Override
    public void verify(String email, String verificationCode) throws InvalidVerificationRequestException {
        if (isRegisteredAndVerifiedUser(email)) {
            throw new InvalidVerificationRequestException("email already registered");
        }

        VerificationCode verifiedUserEmail = verificationCodeRepository.findByEmailAndCode(email, verificationCode)
                .orElseThrow(() -> new InvalidVerificationRequestException("Invalid verification request"));

        verificationCodeRepository.delete(verifiedUserEmail);

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setVerified(true);
        user.setActive(true);
        user.setDateVerified(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public UserData login(String email, String password) throws InvalidLoginCredentialException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow();
            BCrypt.Result verify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (user.isVerified() && user.isActive() && verify.verified) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return UserServiceUtil.toUserData(user);
            }
        } catch (Exception e) {
            throw new InvalidLoginCredentialException("Invalid login credentials", e);
        }

        throw new InvalidLoginCredentialException("Invalid login credentials");
    }

    @Override
    @Transactional
    public List<UserData> getAllUsers(boolean excludeUnverified, boolean excludeInactive) {
        List<UserData> userDataList = new ArrayList<>();
        Stream<User> userStream = userRepository.findAllByIsVerifiedAndIsActive(excludeUnverified, excludeInactive);
        userStream.forEach(user -> userDataList.add(UserServiceUtil.toUserData(user)));

        return userDataList;
    }

    @Override
    public UserData getUser(String id) throws UserNotFoundException {
        User user = getUserById(id);
        return UserServiceUtil.toUserData(user);
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void updateUser(String id, UpdateUserRequest request) throws UserNotFoundException, InvalidUserRegistrationRequestException {
        User user = getUserById(id);

        if (Objects.nonNull(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if (Objects.nonNull(request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (Objects.nonNull(request.getBirthDate())) {
            LocalDate parsedBirthDate = LocalDate.parse(request.getBirthDate());
            UserServiceUtil.validateUserAge(parsedBirthDate);
            user.setBirthDate(parsedBirthDate);
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.withDefaults().hashToString(4, request.getPassword().toCharArray()));
        }

        if (Objects.nonNull(request.getIsActive())) {
            user.setActive(request.getIsActive());
        }

        userRepository.save(user);
    }

    private boolean isRegisteredAndVerifiedUser(String email) {
        Optional<User> emailQ = userRepository.findByEmail(email);
        return emailQ.isPresent() && emailQ.get().isVerified();
    }

    private User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));
    }
}
