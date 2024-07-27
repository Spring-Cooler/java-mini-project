package com.blackjack.member.service;

import com.blackjack.member.aggregate.LoginForm;
import com.blackjack.member.aggregate.Member;
import com.blackjack.member.aggregate.MemberResponseObject;
import com.blackjack.member.repository.MemberRepository;

public class MemberService {

    private final MemberRepository memberRepository = new MemberRepository();

    public MemberResponseObject findMemberByLoginForm(LoginForm loginForm) {
        Member member = memberRepository.selectMemberById(loginForm.getMemId());
        if(member == null) return new MemberResponseObject(null,-1);

        if (member.getPwd().equals(loginForm.getMemPwd())) {
            return new MemberResponseObject(member,1);
        }
        else return new MemberResponseObject(null,0);
    }

    public MemberResponseObject findMemberByMemNo(int memNo) {
        Member member = memberRepository.selectMemberByMemNo(memNo);
        if(member != null) {
            return new MemberResponseObject(member,true);
        }
        else return new MemberResponseObject(null,false);
    }

    public MemberResponseObject findMemberByNickname(String nickname) {
        Member member = memberRepository.selectMemberByNickname(nickname);
        if(member != null) {
            return new MemberResponseObject(member,true);
        }
        else return new MemberResponseObject(null,false);
    }

    public MemberResponseObject registMember(Member member) {
        return new MemberResponseObject(null, memberRepository.insertMember(member));
    }

    public MemberResponseObject findMemberById(String memId) {
        Member member = memberRepository.selectMemberById(memId);
        if(member == null) return new MemberResponseObject(null,false);
        else return new MemberResponseObject(null,true);
    }

    public MemberResponseObject removeMember(Member member) {
        if(member != null) {
            memberRepository.deleteMember(member);
            return new MemberResponseObject(null,true);
        }
        return new MemberResponseObject(null,false);
    }
}
