package com.blackjack.member.repository;

import com.blackjack.member.aggregate.Member;
import com.blackjack.stream.MyObjectOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemberRepository {

    private final ArrayList<Member> memberList = new ArrayList<>();
    private final String filePath = "전기범 미니프로젝트/blackjack/src/main/java/com/blackjack/member/db/memberDB.dat";
    private final File file;
    public MemberRepository() {
        file = new File(filePath);

        if(!file.exists()) {
            saveMembers(file, memberList);
        }

        loadMember(file);
    }

    private void saveMembers(File file, ArrayList<Member> members) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)
                    )
            );

            for(Member member: members) {
                oos.writeObject(member);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(oos != null) oos.close();
            } catch (IOException e) {
                System.out.println("oos close failure");
            }
        }
    }

    private void loadMember(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(file)
                    )
            );

            while(true) {
                memberList.add((Member)ois.readObject());
            }
        } catch (EOFException e) {
            System.out.println("회원 정보 모두 로딩됨...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(ois != null) ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int selectLastMemberNo() {
        if(memberList.isEmpty()) return 0;
        return memberList.get(memberList.size() - 1).getMemNo();
    }

    public boolean insertMember(Member member) {
        MyObjectOutput moo = null;
        boolean result = false;
        try {
            moo = new MyObjectOutput(
                    new BufferedOutputStream(
                            new FileOutputStream(filePath, true)
                    )
            );
            member.setMemNo(selectLastMemberNo()+1);
//            System.out.println(member);
            moo.writeObject(member);

            memberList.add(member);

            result = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(moo != null) moo.close();
            } catch (IOException e) {
                System.out.println("moo close failure");
            }
        }
        return result;
    }

    public Member selectMemberByMemNo(int memNo) {
        int index = Collections.binarySearch(memberList
                        , new Member(memNo, "", "", "", "", 0));
        return new Member(memberList.get(index));
    }

    public Member selectMemberById(String memId) {
        for(Member member : memberList) {
            if(member.getId().equals(memId)) return new Member(member); // 깊은 복사
        }
        return null;
    }

    public Member selectMemberByNickname(String nickname) {
        for(Member member : memberList) {
            if(member.getNickname().equals(nickname)) return new Member(member); // 깊은 복사
        }
        return null;
    }

    public boolean updateMember(Member member) {
        int index = Collections.binarySearch(memberList
                , new Member(member.getMemNo(), "", "", "", "", 0));
        if(index >= 0) {
            memberList.get(index).setNickname(member.getNickname());
            memberList.get(index).setPwd(member.getPwd());
            saveMembers(file, memberList);
            return true;
        }
        return false;
    }

    public boolean deleteMember(Member member) {
        int index = Collections.binarySearch(memberList
                , new Member(member.getMemNo(), "", "", "", "", 0));
        if(index >= 0) {
            memberList.remove(index);
            saveMembers(file, memberList);
            return true;
        }
        return false;
    }

}
