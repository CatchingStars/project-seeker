package ru.homyakin.seeker.locale.duel;

public record DuelResource(
    String duelMustContainsMention,
    String[] duelWithDifferentBot,
    String[] duelWithThisBot,
    String duelWithYourself,
    String[] duelWithInitiatorNotEnoughMoney,
    String personageAlreadyStartDuel,
    String initDuel,
    String[] initDuelVariation,
    String notDuelAcceptingPersonage,
    String[] expiredDuel,
    String[] declinedDuel,
    String[] finishedDuel,
    String acceptDuelButton,
    String declineDuelButton,
    String[] duelWithUnknownUser,
    String duelIsLocked,
    String personageDuelResult,
    String duelAlreadyFinished
) {
}
