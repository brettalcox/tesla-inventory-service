package com.tesla.teslainventoryservice.controller;

import com.tesla.teslainventoryservice.entity.Referral;
import com.tesla.teslainventoryservice.service.ReferralService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReferral(@PathVariable("id") final String id) {
        referralService.deleteReferral(id);
    }

    @GetMapping
    public List<Referral> getAllReferrals() {
        return referralService.getAllReferrals();
    }

    @GetMapping("/random")
    public Referral getReferralCode() {
        return referralService.getRandomReferral();
    }
}
