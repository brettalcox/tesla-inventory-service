package com.tesla.teslainventoryservice.controller;

import com.tesla.teslainventoryservice.entity.Referral;
import com.tesla.teslainventoryservice.service.ReferralService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/referrals")
@RestController
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController(final ReferralService referralService) {
        this.referralService = referralService;
    }

    @PostMapping
    public Referral createReferral(@RequestBody final Referral referral) {
        return referralService.saveReferral(referral);
    }
}
