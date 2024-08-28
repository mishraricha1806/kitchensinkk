package org.example.kitchensinkk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.example.kitchensinkk.exception.ResourceNotFoundException;
import org.example.kitchensinkk.model.Member;
import org.example.kitchensinkk.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mocking the MemberService bean
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        // This is handled by @MockBean and @WebMvcTest, so no need for manual setup
    }

    @Test
    @WithMockUser(roles = "READ_WRITE")
    public void testGetAllMembersApi() throws Exception {
        List<Member> members = Arrays.asList(new Member("1", "John Doe", "john@example.com", "1234567890"));
        when(memberService.getAllMembers()).thenReturn(members);

        mockMvc.perform(get("/members/api"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "READ_WRITE")
    public void testGetMemberByEmailApi() throws Exception {
        Member member = new Member("1", "John Doe", "john@example.com", "1234567890");
        when(memberService.findByEmail("john@example.com")).thenReturn(member);

        mockMvc.perform(get("/members/api/john@example.com"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "READ_WRITE")
    public void testGetMemberByEmailApi_NotFound() throws Exception {
        when(memberService.findByEmail("nonexistent@example.com"))
                .thenThrow(new ResourceNotFoundException("No member found with email"));

        mockMvc.perform(get("/members/api/nonexistent@example.com"))
               .andExpect(status().isNotFound());
    }

	/*
	 * @Test
	 * 
	 * @WithMockUser(roles = "READ_WRITE") // Ensure this role matches the one
	 * expected by Spring Security public void testRegisterNewMember() throws
	 * Exception { mockMvc.perform(post("/members") .param("name", "Jane Doe")
	 * .param("email", "jane@example.com") .param("phoneNumber", "9876543210"))
	 * .andExpect(status().is3xxRedirection())
	 * .andExpect(redirectedUrl("/members")); }
	 */


	/*
	 * @Test
	 * 
	 * @WithMockUser(roles = "READ_WRITE") public void testUpdateMember() throws
	 * Exception { Member member = new Member("1", "John Doe", "john@example.com",
	 * "1234567890"); when(memberService.findById("1")).thenReturn(member);
	 * 
	 * mockMvc.perform(post("/members/update/1") .param("name", "John Doe Updated")
	 * .param("email", "john_updated@example.com") .param("phoneNumber",
	 * "0987654321")) .andExpect(status().is3xxRedirection())
	 * .andExpect(redirectedUrl("/members")); }
	 */}
