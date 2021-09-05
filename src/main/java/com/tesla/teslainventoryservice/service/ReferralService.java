package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.entity.Referral;
import com.tesla.teslainventoryservice.repository.ReferralRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ReferralService {

    private final ReferralRepository referralRepository;

    public ReferralService(final ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    public Referral saveReferral(final Referral referral) {
        return referralRepository.save(referral);
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
