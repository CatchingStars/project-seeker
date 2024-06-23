package ru.homyakin.seeker.game.event.raid.generator;

import java.util.List;

import ru.homyakin.seeker.game.battle.BattlePersonage;

public interface RaidBattleGenerator {
    List<BattlePersonage> generate(List<BattlePersonage> personages);
}
