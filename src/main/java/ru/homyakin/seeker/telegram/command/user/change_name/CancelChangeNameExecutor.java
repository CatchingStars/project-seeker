package ru.homyakin.seeker.telegram.command.user.change_name;

import org.springframework.stereotype.Component;
import ru.homyakin.seeker.game.personage.PersonageService;
import ru.homyakin.seeker.locale.personal.ChangeNameLocalization;
import ru.homyakin.seeker.telegram.TelegramSender;
import ru.homyakin.seeker.telegram.command.CommandExecutor;
import ru.homyakin.seeker.telegram.user.UserService;
import ru.homyakin.seeker.telegram.user.state.UserStateService;
import ru.homyakin.seeker.telegram.utils.ReplyKeyboards;
import ru.homyakin.seeker.telegram.utils.SendMessageBuilder;

@Component
public class CancelChangeNameExecutor extends CommandExecutor<CancelChangeName> {
    private final UserService userService;
    private final UserStateService userStateService;
    private final PersonageService personageService;
    private final TelegramSender telegramSender;

    public CancelChangeNameExecutor(
        UserService userService,
        UserStateService userStateService, PersonageService personageService,
        TelegramSender telegramSender
    ) {
        this.userService = userService;
        this.userStateService = userStateService;
        this.personageService = personageService;
        this.telegramSender = telegramSender;
    }

    @Override
    public void execute(CancelChangeName command) {
        final var user = userService.forceGetFromPrivate(command.userId());
        personageService.cancelChangeName(user.personageId());
        userStateService.clearUserState(user);
        telegramSender.send(SendMessageBuilder.builder()
            .chatId(user.id())
            .text(ChangeNameLocalization.cancelChangeName(user.language()))
            .keyboard(ReplyKeyboards.receptionDeskKeyboard(user.language()))
            .build()
        );
    }
}
