package org.example.kitchensinkk.controller;

import java.util.List;

import org.example.kitchensinkk.exception.ResourceNotFoundException;
import org.example.kitchensinkk.model.Member;
import org.example.kitchensinkk.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/members")
public class MemberController {
	
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // REST API Endpoint to get all members as JSON
    @GetMapping("/api")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_READ_ONLY', 'ROLE_READ_WRITE')")  // Accessible to both READ_ONLY and READ_WRITE roles
    public List<Member> getAllMembersApi() {
        logger.info("Fetching all members");
        return memberService.getAllMembers();
    }

   // REST API Endpoint to get a member by email as JSON
    @GetMapping("/api/{email}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_READ_ONLY', 'ROLE_READ_WRITE')")  // Accessible to both READ_ONLY and READ_WRITE roles
    public ResponseEntity<Member> getMemberByEmailApi(@PathVariable String email) {
        Member member = memberService.findByEmail(email);
        if (member==null) {
            logger.error("No Member found with given mail id: {}", email);
            throw new ResourceNotFoundException("No Member found with given mail id");
        }
        logger.info("Fetching member with email: {}", email);
            return ResponseEntity.ok(member);


    }

    // View all members - requires READ_WRITE role
    @GetMapping
    @PreAuthorize("hasRole('ROLE_READ_WRITE')")  // Requires READ_WRITE role
    public String getAllMembers(Model model) {
        logger.info("Displaying all members view");
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("newMember", new Member());
        return "members";
    }

    // Register a new member - requires READ_WRITE role
    @PostMapping
    @PreAuthorize("hasRole('ROLE_READ_WRITE')")  // Requires READ_WRITE role
    public String register(@Valid @ModelAttribute("newMember") Member newMember, BindingResult result, Model model) {
        logger.info("Registering new member: {}", newMember);
        if (result.hasErrors()) {
            logger.error("Invalid member data: {}", newMember);
            model.addAttribute("members", memberService.getAllMembers());
            return "members"; // Return to the form view to show validation errors
        }
        
        memberService.saveMember(newMember);
        return "redirect:/members";
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        logger.info("Returning login page");
        return "login"; // This should match the name of the HTML file without the .html extension
    }
    
    @GetMapping("/error")
    public String showErrorPage() {
        logger.info("Returning error page");
        return "error"; // This should match the name of the HTML file without the .html extension
    }
    
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_READ_WRITE')")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Member member = memberService.findById(id);
        if (member == null) {
            logger.error("No member found with id: {}", id);
            throw new ResourceNotFoundException("No member found with id: " + id);
        }
        model.addAttribute("member", member);
        return "edit-member";
    }
    
    // Update member details - requires READ_WRITE role
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_READ_WRITE')")
    public String updateMember(@PathVariable("id") String id, @Valid @ModelAttribute("member") Member updatedMember, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Invalid member data: {}", updatedMember);
            model.addAttribute("member", updatedMember);
            return "edit-member";
        }

        Member existingMember = memberService.findById(id);
        if (existingMember == null) {
            logger.error("No member found with id: {}", id);
            throw new ResourceNotFoundException("No member found with id: " + id);
        }

        // Set the ID to ensure the update occurs on the correct member
        updatedMember.setId(id);

        // Perform the update
        memberService.saveMember(updatedMember);

        logger.info("Updated member with id: {}", id);
        return "redirect:/members";
    }
}
