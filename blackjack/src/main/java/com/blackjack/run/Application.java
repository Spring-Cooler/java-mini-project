package com.blackjack.run;

import com.blackjack.member.aggregate.LoginForm;
import com.blackjack.member.aggregate.Member;
import com.blackjack.member.aggregate.MemberResponseObject;
import com.blackjack.member.service.MemberService;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Scanner;

import static java.lang.System.exit;

public class Application {

    private static final MemberService memberService = new MemberService();
    private static int memNo;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("\n====== Black Jack ======");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("9. 게임 종료");
            System.out.println("==========================");
            System.out.println("번호를 입력해주세요: ");

            String line = sc.nextLine();
            if(line.length() > 1) {
                System.out.println("\n잘못된 입력입니다.");
                continue;
            }
            try {
                int input = Integer.parseInt(line);
                switch (input) {
                    case 1:
                        MemberResponseObject checkLoginSuccess = memberService.findMemberByLoginForm(memberLogin());
                        if (checkLoginSuccess.getCheckValueInt() > 0) {
                            System.out.println("\n로그인 성공!");
//                        System.out.println(checkLoginSuccess.getMember());
                            memNo = checkLoginSuccess.getMember().getMemNo();
                            goLounge();
                            break;
                        } else if (checkLoginSuccess.getCheckValueInt() == 0) {
                            System.out.println("\n아이디 또는 비밀번호가 잘못되었습니다.");
                        } else {
                            System.out.println("\n존재하지 않는 아이디입니다.");
                        }
                        break;
                    case 2:
                        MemberResponseObject checkSignUpSuccess = memberService.registMember(signUp());
                        if (checkSignUpSuccess.getCheckValueBoolean()) {
                            System.out.println("\n회원가입 성공!");
                        } else {
                            System.out.println("\n회원가입에 실패했습니다.");
                        }
                        break;
                    case 9:
                        System.out.println("\n게임을 종료합니다.");
                        return;
                    default:
                        System.out.println("\n잘못된 입력입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static void clearConsole() {
        try {
            try (Terminal terminal = TerminalBuilder.terminal()) {
                terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                terminal.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("콘솔 화면이 지워졌습니다!");
    }

    private static void goLounge() {
        Scanner sc = new Scanner(System.in);
        System.out.println(memNo);
        while (true) {
            System.out.println("\n====== 라운지 ======");
            System.out.println("1. 게임 시작");
            System.out.println("2. 내 정보 보기");
            System.out.println("3. 회원 검색");
            System.out.println("8. 로그아웃");
            System.out.println("9. 게임 종료");
            System.out.println("0. 회원 탈퇴");
            System.out.println("======================");
            System.out.println("번호를 입력해주세요: ");
            String line = sc.nextLine();
            if(line.length() > 1) {
                System.out.println("\n잘못된 입력입니다.");
                continue;
            }
            try {
                int input = Integer.parseInt(line);
                switch (input) {
                    case 1:
                        break;
                    case 2:
                        MemberResponseObject checkMyInfoSuccess = memberService.findMemberByMemNo(memNo);
                        if (checkMyInfoSuccess.getCheckValueBoolean()) {
                            printMemInfo(checkMyInfoSuccess);
                        } else {
                            System.out.println("\n내 정보를 불러오지 못했습니다.");
                        }
                        break;
                    case 3:
                        MemberResponseObject checkMemInfoSuccess = memberService.findMemberByNickname(searchNickname());
                        if (checkMemInfoSuccess.getCheckValueBoolean()) {
                            printMemInfo(checkMemInfoSuccess);
                        } else {
                            System.out.println("\n존재하지 않는 닉네임입니다.");
                        }
                        break;
                    case 8:
                        System.out.println("\n로그아웃 완료");
                        return;
                    case 9:
                        System.out.println("\n게임을 종료합니다.");
                        exit(0);
                    case 0:
                        MemberResponseObject checkMemDelSuccess = memberService.removeMember(delAccount());
                        if (checkMemDelSuccess.getCheckValueBoolean()) {
                            System.out.println("\n회원 탈퇴 완료");
                            return;
                        }
                        else {
                            System.out.println("\n회원 탈퇴 실패");
                            break;
                        }
                    default:
                        System.out.println("\n잘못된 입력입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static Member delAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n비밀번호 입력: ");
        String memPwd = sc.nextLine();
        System.out.println("[계정 삭제]를 입력하세요: ");
        String checkConfirm = sc.nextLine();
        if(checkConfirm.equals("계정 삭제")) {
            MemberResponseObject checkPwdValid = memberService.findMemberByMemNo(memNo);
            if(memPwd.equals(checkPwdValid.getMember().getPwd())) return checkPwdValid.getMember();
            else {
                System.out.println("\n비밀번호가 다릅니다.");
                return null;
            }
        }
        System.out.println("\n문장을 잘못 입력하셨습니다.");
        return null;
    }

    private static void printMemInfo(MemberResponseObject mro) {
        System.out.println("\n====== 회원 정보 ======");
//        System.out.println("회원번호: " + mro.getMember().getMemNo());
        System.out.println("닉네임: " + mro.getMember().getNickname());
        System.out.println("나이: " + mro.getMember().getAge());
        System.out.println("잔고: $" + mro.getMember().getDollars());
        System.out.println("====================");
    }

    private static String searchNickname() {
        Scanner sc = new Scanner(System.in);
        System.out.println("검색할 회원의 닉네임을 입력하세요: ");
        return sc.nextLine();
    }

    private static Member signUp() {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        String memId;
        do {
            System.out.println("\n아이디: ");
            memId = sc.nextLine();
            MemberResponseObject checkValidId = memberService.findMemberById(memId);
            if (!checkValidId.getCheckValueBoolean()) flag = false;
            else System.out.println("\n중복된 아이디입니다.");
        } while(flag);

        String memPwd;
        String memPwdConfirm;
        flag = true;
        do {
            System.out.println("비밀번호: ");
            memPwd = sc.nextLine();
            System.out.println("비밀번호 확인: ");
            memPwdConfirm = sc.nextLine();
            if(memPwd.equals(memPwdConfirm)) flag = false;
            else System.out.println("\n비밀번호가 서로 일치하지 않습니다.\n");
        } while(flag);

        System.out.println("이름: ");
        String memName = sc.nextLine();

        String memNickname;
        flag = true;
        do {
            System.out.println("닉네임: ");
            memNickname = sc.nextLine();
            MemberResponseObject checkValidNickname = memberService.findMemberByNickname(memNickname);
            if(!checkValidNickname.getCheckValueBoolean()) flag = false;
            else System.out.println("\n중복된 닉네임입니다.\n");
        } while(flag);

        System.out.println("나이: ");
        int memAge = sc.nextInt();
        sc.nextLine(); //\n 비우기
        return new Member(memId, memPwd, memName, memNickname, memAge);
    }

    private static LoginForm memberLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n====== 로그인 ======");
        System.out.println("아이디: ");
        String memId = sc.nextLine();
        System.out.println("비밀번호: ");
        String memPwd = sc.nextLine();
        return new LoginForm(memId, memPwd);
    }
}