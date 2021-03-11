package ph.apper.service;

import org.springframework.stereotype.Service;
import ph.apper.domain.User;
import ph.apper.exception.InvalidUserRegistrationRequestException;
import ph.apper.payload.UserData;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public final class UserServiceUtil {

    public static void validateUserAge(LocalDate parsedBirthDate) throws InvalidUserRegistrationRequestException {
        Period periodDiff = Period.between(parsedBirthDate, LocalDate.now());
        if (periodDiff.getYears() < 18) {
            throw new InvalidUserRegistrationRequestException("age must be at least 18");
        }
    }

    public static UserData toUserData(User u) {
        UserData userData = new UserData();
        userData.setBirthDate(u.getBirthDate().format(DateTimeFormatter.ISO_DATE));
        userData.setFirstName(u.getFirstName());
        userData.setLastName(u.getLastName());
        userData.setEmail(u.getEmail());
        userData.setVerified(u.isVerified());
        userData.setActive(u.isActive());
        userData.setDateRegistered(u.getDateRegistered().format(DateTimeFormatter.ISO_DATE_TIME));
        userData.setId(u.getId());

        if (Objects.nonNull(u.getLastLogin())) {
            userData.setLastLogin(u.getLastLogin().format(DateTimeFormatter.ISO_DATE_TIME));
        }

        if (Objects.nonNull(u.getDateVerified())) {
            userData.setDateVerified(u.getDateVerified().format(DateTimeFormatter.ISO_DATE_TIME));
        }

        return userData;
    }
}
