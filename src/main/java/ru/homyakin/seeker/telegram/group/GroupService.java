package ru.homyakin.seeker.telegram.group;

import io.vavr.control.Either;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.homyakin.seeker.locale.Language;
import ru.homyakin.seeker.telegram.group.database.GroupDao;
import ru.homyakin.seeker.telegram.group.database.GroupMigrateDao;
import ru.homyakin.seeker.telegram.group.models.ActiveTime;
import ru.homyakin.seeker.telegram.group.models.ActiveTimeError;
import ru.homyakin.seeker.telegram.group.models.Group;
import ru.homyakin.seeker.telegram.group.models.GroupId;
import ru.homyakin.seeker.telegram.group.stats.GroupStatsService;
import ru.homyakin.seeker.utils.models.Success;

@Service
public class GroupService {
    private final GroupDao groupDao;
    private final GroupMigrateDao groupMigrateDao;
    private final GroupStatsService groupStatsService;

    public GroupService(GroupDao groupDao, GroupMigrateDao groupMigrateDao, GroupStatsService groupStatsService) {
        this.groupDao = groupDao;
        this.groupMigrateDao = groupMigrateDao;
        this.groupStatsService = groupStatsService;
    }

    public Group getOrCreate(GroupId groupId) {
        return getGroup(groupId)
            .map(group -> group.activate(groupDao))
            .orElseGet(() -> createGroup(groupId));
    }

    public void setNotActive(GroupId groupId) {
        getGroup(groupId).map(group -> group.deactivate(groupDao));
    }

    public Group changeLanguage(Group group, Language language) {
        return group.changeLanguage(language, groupDao);
    }

    public List<Group> getGetGroupsWithLessNextEventDate(LocalDateTime maxNextEventDate) {
        return groupDao.getGetGroupsWithLessNextEventDate(maxNextEventDate);
    }

    public List<Group> getGetGroupsWithLessNextRumorDate(LocalDateTime maxNextRumorDate) {
        return groupDao.getGetGroupsWithLessNextRumorDate(maxNextRumorDate);
    }

    public void updateNextEventDate(Group group, LocalDateTime nextEventDate) {
        groupDao.updateNextEventDate(group.id(), nextEventDate);
    }

    public void updateNextRumorDate(Group group, LocalDateTime nextRumorDate) {
        groupDao.updateNextRumorDate(group.id(), nextRumorDate);
    }

    public Either<ActiveTimeError, Success> updateActiveTime(Group group, int startHour, int endHour, int timeZone) {
        return ActiveTime.from(startHour, endHour, timeZone)
            .map(group::withActiveTime)
            .peek(groupDao::update)
            .map(_ -> Success.INSTANCE);
    }

    public void migrateGroupDate(GroupId from, GroupId to) {
        groupMigrateDao.migrate(from, to);
    }

    private Optional<Group> getGroup(GroupId group) {
        return groupDao.getById(group);
    }

    private Group createGroup(GroupId groupId) {
        final var group = new Group(groupId, true, Language.DEFAULT, ActiveTime.createDefault());
        groupDao.save(group);
        groupStatsService.create(groupId);
        return group;
    }
}
