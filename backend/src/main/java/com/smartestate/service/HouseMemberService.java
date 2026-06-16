package com.smartestate.service;

import com.smartestate.entity.HouseMember;

import java.util.List;

public interface HouseMemberService {
    List<HouseMember> listByHouse(Long userId, String role);
    List<HouseMember> listAll(String role);
    HouseMember add(Long ownerUserId, String role, com.smartestate.dto.HouseMemberAddRequest request);
    HouseMember remove(Long id, Long ownerUserId, String role);
}
