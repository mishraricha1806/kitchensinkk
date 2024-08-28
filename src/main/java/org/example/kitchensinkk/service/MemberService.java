package org.example.kitchensinkk.service;

import org.example.kitchensinkk.exception.ResourceNotFoundException;
import org.example.kitchensinkk.model.Member;
import org.example.kitchensinkk.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public List<Member> getAllMembers() {
		logger.info("Retrieving all members from repository");
		return memberRepository.findAll();
	}

	public Member saveMember(Member member) {
		logger.info("Saving member: {}", member);
		return memberRepository.save(member);
	}

	public Member findByEmail(String email) {
		logger.info("Finding member by email: {}", email);
		Member member = memberRepository.findByEmail(email);
		if (member == null) {
			logger.error("Member with email {} not found", email);
			throw new ResourceNotFoundException("Member with email " + email + " not found");
		}
		return member;
	}

	
	public Member findById(String id) {
		return memberRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Member with id " + id + " not found"));
	}
}
