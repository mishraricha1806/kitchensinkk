
package org.example.kitchensinkk.service;

import org.example.kitchensinkk.model.Member;
import org.example.kitchensinkk.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)

@DataMongoTest
public class MemberServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	private MemberService memberService;

	@BeforeEach void setUp() { memberService = new
  MemberService(memberRepository); memberRepository.deleteAll(); // Ensure thedatabase is clean before each test 
  }

	@Test
	void testSaveMember() {
		Member member = new Member(null, "John Doe", "john.doe@example.com", "1234567890");
		Member savedMember = memberService.saveMember(member);

		assertThat(savedMember).isNotNull();
		assertThat(savedMember.getId()).isNotNull();
		assertThat(savedMember.getName()).isEqualTo("John Doe");
	}

	@Test
	void testFindByEmail() {
		Member member = new Member(null, "Jane Doe", "jane.doe@example.com", "0987654321");
		memberService.saveMember(member);

		Member foundMember = memberService.findByEmail("jane.doe@example.com");

		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getName()).isEqualTo("Jane Doe");
	}

	@Test
	void testGetAllMembers() {
		Member member1 = new Member(null, "Alice", "alice@example.com", "1111111111");
		Member member2 = new Member(null, "Bob", "bob@example.com", "2222222222");
		memberService.saveMember(member1);
		memberService.saveMember(member2);

		assertThat(memberService.getAllMembers()).hasSize(2);
	}
}
