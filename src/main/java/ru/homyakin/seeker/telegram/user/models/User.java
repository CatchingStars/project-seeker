package ru.homyakin.seeker.telegram.user.models;

import ru.homyakin.seeker.game.personage.models.PersonageId;
import ru.homyakin.seeker.locale.Language;
import ru.homyakin.seeker.telegram.user.UserDao;
import ru.homyakin.seeker.telegram.user.entity.Username;

import java.util.Optional;

public record User(
    UserId id,
    boolean isActivePrivateMessages,
    Language language,
    PersonageId personageId,
    Optional<Username> username
) {
    // TODO убрать dao из класса
    public boolean isSameLanguage(Language newLanguage) {
        return language == newLanguage;
    }

    public User changeLanguage(Language newLanguage, UserDao userDao) {
        if (!isSameLanguage(newLanguage)) {
            final var user = new User(
                id,
                isActivePrivateMessages,
                newLanguage,
                personageId,
                username
            );
            userDao.update(user);
            return user;
        }
        return this;
    }

    public User deactivatePrivateMessages(UserDao userDao) {
        if (isActivePrivateMessages) {
            final var user = new User(
                id,
                false,
                language,
                personageId,
                username
            );
            userDao.update(user);
            return user;
        }
        return this;
    }

    public User activatePrivateMessages(UserDao userDao) {
        if (!isActivePrivateMessages) {
            final var user = new User(
                id,
                true,
                language,
                personageId,
                username
            );
            userDao.update(user);
            return user;
        }
        return this;
    }
}
