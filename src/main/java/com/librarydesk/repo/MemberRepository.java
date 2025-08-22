package com.librarydesk.repo;

import com.librarydesk.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void add(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
    List<Member> search(String query);
}
