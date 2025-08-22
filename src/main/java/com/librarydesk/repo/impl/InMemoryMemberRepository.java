package com.librarydesk.repo.impl;

import com.librarydesk.domain.Member;
import com.librarydesk.repo.MemberRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryMemberRepository implements MemberRepository {
    private final Map<String, Member> members = new LinkedHashMap<>();

    @Override
    public void add(Member member) {
        members.put(member.getId(), member);
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(members.values());
    }

    @Override
    public List<Member> search(String query) {
        String q = query.toLowerCase();
        return members.values().stream()
                .filter(m -> m.getName().toLowerCase().contains(q) ||
                        m.getEmail().toLowerCase().contains(q) ||
                        m.getPhone().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}
