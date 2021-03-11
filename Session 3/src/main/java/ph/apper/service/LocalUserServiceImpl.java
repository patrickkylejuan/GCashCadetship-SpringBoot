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
import ph.apper.util.IdService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Profile({"local"})
@Service
public class LocalUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUserServiceImpl.class);

    private final List<User> users = new ArrayList<>();
    private final List<VerificationCode> verificationCodes = new ArrayList<>();

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

        users.add(newUser);
        verificationCodes.add(verificationCode);

        return new UserRegistrationResponse(code);
    }

    @Override
    public void verify(String email, String code) throws InvalidVerificationRequestException {
        if (isRegisteredAndVerifiedUser(email)) {
            throw new InvalidVerificationRequestException("email already registered");
        }

        VerificationCode verifiedUserEmail = verificationCodes.stream()
                .filter(verificationCode -> email.equals(verificationCode.getEmail()) && code.equals(verificationCode.getCode()))
                .findFirst()
                .orElseThrow(() -> new InvalidVerificationRequestException("Invalid verification request"));

        verificationCodes.remove(verifiedUserEmail);

        User user = getUserByEmail(email);
        user.setVerified(true);
        user.setActive(true);
        user.setDateVerified(LocalDateTime.now());
    }

    @Override
    public UserData login(String email, String password) throws InvalidLoginCredentialException {
        try {
            User user = getUserByEmail(email);
            BCrypt.Result verify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (user.isVerified() && user.isActive() && verify.verified) {
                user.setLastLogin(LocalDateTime.now());
                return UserServiceUtil.toUserData(user);
            }
        } catch (Exception e) {
            throw new InvalidLoginCredentialException("Invalid login credentials", e);
        }

        throw new InvalidLoginCredentialException("Invalid login credentials");
    }

    @Override
    public List<UserData> getAllUsers(boolean excludeUnverified, boolean excludeInactive) {
        List<UserData> userDataList = new ArrayList<>();
        Stream<User> userStream = users.stream();

        if (excludeUnverified) {
            userStream = userStream.filter(User::isVerified);
        }

        if (excludeInactive) {
            userStream = userStream.filter(User::isActive);
        }

        userStream.forEach(user -> userDataList.add(UserServiceUtil.toUserData(user)));

        return userDataList;
    }

    @Override
    public UserData getUser(String id) throws UserNotFoundException {
        User u = getUserById(id);

        return UserServiceUtil.toUserData(u);
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
        User user = getUserById(id);
        user.setActive(false);
    }

    @Override
    public void updateUser(String id, UpdateUserRequest request) throws UserNotFoundException {

    }

    private boolean isRegisteredAndVerifiedUser(String email) {
        try {
            return getUserByEmail(email).isVerified();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private User getUserById(String id) throws UserNotFoundException {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));
    }

    private User getUserByEmail(String email) {
        return users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElseThrow();
    }
}
