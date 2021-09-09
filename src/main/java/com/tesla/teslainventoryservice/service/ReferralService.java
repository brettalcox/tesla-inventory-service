package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.entity.Referral;
import com.tesla.teslainventoryservice.repository.ReferralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ReferralService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferralService.class);

    private final ReferralRepository referralRepository;

    public ReferralService(final ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    public Referral saveReferral(final Referral referral) {
        return referralRepository.save(referral);
    }

    public void deleteReferral(final String id) {
        try {
            referralRepository.deleteById(id);
        } catch (final EmptyResultDataAccessException e) {
            LOGGER.info("Referral with id {} does not exist. Nothing to delete...", id, e);
        }
    }

    public List<Referral> getAllReferrals() {
        return referralRepository.findAll();
    }

    public String getReferralCode() {
        final Random rand = new Random();
        final List<Referral> referrals = referralRepository.findAll();
        if (referrals.size() > 0) {
            return referrals.get(rand.nextInt(referrals.size())).getCode();
        }
        return null;
    }
}
