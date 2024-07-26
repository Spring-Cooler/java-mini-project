package testapp.service;

import testapp.aggregate.Member;
import testapp.repository.MemberRepository;

import java.util.ArrayList;

/* 설명. 트랜잭션 처리(성공실패에 따른 commit/rollback) 및 회원관련 비즈니스 로직 처리 */
public class MemberService {


    private final MemberRepository mr =new MemberRepository();

    public MemberService(){}
    public void findAllMembers(){
        ArrayList<Member> findMembers = mr.selectAllMembers();

        for(Member member : findMembers){
            System.out.println("member = " + member);
        }
    }

    public void findMemberBy(int memNo) {
        Member selectedMember= mr.selectMemberBy(memNo);

        if(selectedMember != null){
            System.out.println("조회된 회원은: " + selectedMember.getId() + " 아이디 회원");
        }else{
            System.out.println("그런 회원은 없어요!~");
        }
    }

    public void registMember(Member newMember) {
        /* 수업목표. 객체 파일 출력으로 insert 개념을 진행할 때는 기존 회원에 이어서 출력이 발생해야 함(feat.MyObjectOutput)*/
        int lastMemberNo = mr.selectLastMemberNo();
        newMember.setMemNo(lastMemberNo+1);
        int result =mr.insertMember(newMember);
        if(result == 1){
            System.out.println(newMember.getId()+"님 회원가입 해 주셔서 감사합니다.");

            /* 설명. JDBC 이후에는 commit/rollback 처리도 할 예정 */
        }
    }

    public void removeMember(int removeMember) {
        int result = mr.deleteMember(removeMember);
        if(result ==1){
            System.out.println("다시 돌아오세요!~");
            return;
        }
        System.out.println("회원 번호 틀렸나 본데요?");
    }

    public void updateMember(int updateMember) {
        int result= mr.updateMember(updateMember);
        if (result==1){
            System.out.println("회원 정보 수정이 완료되었습니다");
        }

    }

    public void login() {
        int loginresult= mr.login();
        if (loginresult==2){
            System.out.println("로그인이 완료되었습니다");
        }
        else if(loginresult==1){
            System.out.println("비밀 번호를 확인해주세요.");
        }
        else{
            System.out.println("아이디를 확인해주세요.");
        }

    }
}
