package ph.apper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.apper.exception.InvalidLoginCredentialException;
import ph.apper.exception.InvalidUserRegistrationRequestException;
import ph.apper.exception.InvalidVerificationRequestException;
import ph.apper.exception.UserNotFoundException;
import ph.apper.payload.GenericResponse;
import ph.apper.payload.LoginRequest;
import ph.apper.payload.UpdateUserRequest;
import ph.apper.payload.UserData;
import ph.apper.payload.UserRegistrationRequest;
import ph.apper.payload.UserRegistrationResponse;
import ph.apper.payload.VerificationRequest;
import ph.apper.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserRegistrationResponse> register(
            @Valid @RequestBody UserRegistrationRequest request,
            @RequestHeader("Reference-Number") String referenceNumber) throws InvalidUserRegistrationRequestException {
        LOGGER.info("User registration {} received", referenceNumber);

        UserRegistrationResponse response = userService.register(request);

        LOGGER.info("User registration {} successful", referenceNumber);

        return ResponseEntity.ok(response);
    }

    @PostMapping("verify")
    public ResponseEntity<GenericResponse> verify(
            @Valid @RequestBody VerificationRequest request,
            @RequestHeader("Reference-Number") String referenceNumber) throws InvalidVerificationRequestException {
        LOGGER.info("User verification {} received", referenceNumber);

        userService.verify(request.getEmail(), request.getVerificationCode());

        LOGGER.info("User verification {} successful", referenceNumber);

        return ResponseEntity.ok(new GenericResponse("verification success"));
    }

    @PostMapping("login")
    public ResponseEntity<UserData> login(
            @Valid @RequestBody LoginRequest request,
            @RequestHeader("Reference-Number") String referenceNumber) throws InvalidLoginCredentialException {

        LOGGER.info("User login {} received", referenceNumber);

        UserData user = userService.login(request.getEmail(), request.getPassword());

        LOGGER.info("User login {} successful", referenceNumber);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserData>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers(true, true));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserData> getUser(@PathVariable("id") String userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<GenericResponse> deleteUser(
            @PathVariable("id") String userId,
            @RequestHeader("Reference-Number") String referenceNumber) throws UserNotFoundException {
        LOGGER.info("Deleting user with reference number {} received", referenceNumber);

        userService.deleteUser(userId);

        LOGGER.info("Delete user with reference number {} successful", referenceNumber);

        return ResponseEntity.ok(new GenericResponse("delete user success"));
    }

    @PatchMapping("{id}")
    public ResponseEntity<GenericResponse> updateUser(
            @PathVariable String id,
            @RequestHeader("Reference-Number") String referenceNumber,
            @Valid @RequestBody UpdateUserRequest request) throws UserNotFoundException, InvalidUserRegistrationRequestException {
        LOGGER.info("Update user with reference number {} received", referenceNumber);

        userService.updateUser(id, request);

        LOGGER.info("Update user with reference number {} successful", referenceNumber);

        return ResponseEntity.ok(new GenericResponse("update user success"));
    }
}
