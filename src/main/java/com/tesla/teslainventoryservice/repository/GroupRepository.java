package com.tesla.teslainventoryservice.repository;

import com.tesla.teslainventoryservice.entity.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<Group, String> {
    List<Group> findAll();
}
