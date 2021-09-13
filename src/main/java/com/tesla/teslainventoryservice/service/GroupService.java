package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.entity.Group;
import com.tesla.teslainventoryservice.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(final GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public Group createGroup(final Group group) {
        return groupRepository.save(group);
    }
}
