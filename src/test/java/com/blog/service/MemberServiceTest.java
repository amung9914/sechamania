package com.blog.service;

import com.blog.dto.AddUserRequest;
import com.blog.entity.Member;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void join() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("test", "test", "1234", "add","r","city", "lat", "lon");

        // when
        memberService.join(request1,null);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findByEmail("test");

        // then
        Assertions.assertThat(findMember.get().getAddress().getFullAddress()).isEqualTo("addr");

    }

    @Test
    public void joinForCompany() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("test", "test", "1234", "add","r","city", "lat", "lon");

        // when
        memberService.joinForCompany(request1,null);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findByNickname("nick1");
        Collection<? extends GrantedAuthority> authorities = findMember.get().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println("authority = " + authority);
        }

        // then
        Assertions.assertThat(findMember.get().getEmail()).isEqualTo(request1.getEmail());
    }

    @Test
    public void duplicatedEmail() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("test", "test", "1234", "add","r","city", "lat", "lon");
        // when
        memberService.join(request1,null);
        em.flush();
        em.clear();
        AddUserRequest request2 = new AddUserRequest("test1", "test1", "1234", "add","r","city", "lat", "lon");

        //then
        assertThrows(IllegalStateException.class, () ->{
            memberService.join(request2,null);
        });


    }

    @Test
    public void duplicatedNickname() throws Exception {
        // given
        AddUserRequest request1 = new AddUserRequest("test", "test", "1234", "add","r","city", "lat", "lon");
        // when
        memberService.join(request1,null);
        em.flush();
        em.clear();
        AddUserRequest request2 = new AddUserRequest("test1", "test1", "1234", "add","r","city", "lat", "lon");

        //then
        assertThrows(IllegalStateException.class, () ->{
            memberService.join(request2,null);
        });

    }



}