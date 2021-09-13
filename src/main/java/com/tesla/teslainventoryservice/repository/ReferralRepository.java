package com.tesla.teslainventoryservice.repository;

import com.tesla.teslainventoryservice.entity.Referral;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends CrudRepository<Referral, String> {
    List<Referral> findAll();

    List<Referral> findAllByGroupId(String groupId);
}
