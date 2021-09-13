package com.tesla.teslainventoryservice.controller;

import com.tesla.teslainventoryservice.entity.Group;
import com.tesla.teslainventoryservice.model.ReferralRequest;
import com.tesla.teslainventoryservice.service.GroupService;
import com.tesla.teslainventoryservice.service.ReferralService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/groups")
@RestController
public class GroupController {

    private final GroupService groupService;

    private final ReferralService referralService;

    public GroupController(final GroupService groupService, final ReferralService referralService) {
        this.groupService = groupService;
        this.referralService = referralService;
    }

    @PostMapping
    public Group createGroup(@RequestBody final Group group) {
        return groupService.createGroup(group);
    }

    @PostMapping("/{groupId}/referrals")
    public void addReferralToGroup(@PathVariable("groupId") final String groupId, @RequestBody final ReferralRequest referralRequest) {
        referralService.addReferralToGroup(groupId, referralRequest);
    }
}
