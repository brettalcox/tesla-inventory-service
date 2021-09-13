package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.entity.Group;
import com.tesla.teslainventoryservice.entity.Referral;
import com.tesla.teslainventoryservice.model.ReferralRequest;
import com.tesla.teslainventoryservice.repository.GroupRepository;
import com.tesla.teslainventoryservice.repository.ReferralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ReferralService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferralService.class);

    private final ReferralRepository referralRepository;

    private final GroupRepository groupRepository;

    public ReferralService(final ReferralRepository referralRepository, final GroupRepository groupRepository) {
        this.referralRepository = referralRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public Referral saveReferral(final Referral referral) {
        return referralRepository.save(referral);
    }

    @Transactional
    public void deleteReferral(final String id) {
        try {
            referralRepository.deleteById(id);
        } catch (final EmptyResultDataAccessException e) {
            LOGGER.info("Referral with id {} does not exist. Nothing to delete...", id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<Referral> getAllReferrals() {
        return referralRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Referral getRandomReferral() {
        // i hate myself for this. but it's also a zero maintenance/no input group rotation that dynamically
        // recreates the weekly schedule as groups are added and removed
        final LocalDate date = LocalDate.now();
        int weekOfYear = date.get(WeekFields.ISO.weekOfYear());
        final List<Group> groups = groupRepository.findAll();
        final Supplier<Stream<Group>> groupNames = () -> groups
                .stream()
                .sorted(Comparator.comparing(Group::getName));
        final int streamCount = 52 / groups.size() + 1;
        final List<Group> nGroups = IntStream.range(0, streamCount)
                .mapToObj(i -> groupNames.get())
                .flatMap(i -> i)
                .collect(Collectors.toList());
        final Group selectedGroup = nGroups.get(weekOfYear - 1);

        final Random rand = new Random();
        final List<Referral> referrals = referralRepository.findAllByGroupId(selectedGroup.getId());
        if (referrals.size() > 0) {
            return referrals.get(rand.nextInt(referrals.size()));
        } else {
            final List<Referral> allReferrals = referralRepository.findAll();
            return allReferrals.get(rand.nextInt(referrals.size()));
        }
    }

    @Transactional
    public void addReferralToGroup(final String groupId, final ReferralRequest referralRequest) {
        final Referral referral = referralRepository.findById(referralRequest.getReferralId()).orElseThrow();
        referral.setGroupId(groupId);
        referralRepository.save(referral);
    }
}
