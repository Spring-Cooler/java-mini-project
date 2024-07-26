package testapp.run;

import testapp.aggregate.BloodType;
import testapp.aggregate.Member;
import testapp.service.MemberService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Application {
    private static final MemberService ms = new MemberService();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("====== 회원 관리 프로그램 ======");
            System.out.println("1. 모든 회원 정보 보기");
            System.out.println("2. 회원 찾기");
            System.out.println("3. 회원 가입");
            System.out.println("4. 회원 탈퇴");
            System.out.println("5. 회원 수정");
            System.out.println("6. 회원 로그인");
            System.out.println("9. 프로그램 종료");
            System.out.print("메뉴를 선택해 주세요 ");
            int input = sc.nextInt();

            switch (input){
                case 1: ms.findAllMembers(); break;
                case 2: ms.findMemberBy(chooseMemNo()); break;
                case 3: ms.registMember(signUp()); break;
                case 4: ms.removeMember(chooseMemNo()); break;
                case 5: ms.updateMember(chooseUpdateMemNo()); break;
                case 6: ms.login(); break;
                case 9:
                    System.out.println("회원관리 프로그램을 종료합니다.");return;
                default:
                    System.out.println("번호를 잘못 입력하셨습니다.");
            }
        }
    }
    private static Member signUp() {
        Member newMember = null;

        Scanner sc = new Scanner(System.in);
        System.out.print("아이디를 입력하세요: ");
        String id =sc.nextLine();

        System.out.print("패스워드를 입력하세요: ");
        String pwd = sc.nextLine();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String input=pwd;
            byte[] hashInBytes = md.digest(input.getBytes());
            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        System.out.print("나이를 입력하세요: ");
        int age = sc.nextInt();

        System.out.print("입력할 취미 개수를 입력하세요(숫자로 1 이상): ");
        int length = sc.nextInt();
        sc.nextLine();          // 버퍼의 개행문자 처리용

        String[] hobbies = new String[length];
        for(int i = 0; i < hobbies.length; i++){
            System.out.println((i+1)+"번째 취미를 입력하세요: ");
            String input = sc.nextLine();
            hobbies[i]=input;
        }
        System.out.println("혈액형을 입력하세요(A, AB, B, O): ");
        String bloodType = sc.nextLine().toUpperCase();
        BloodType bt = null;
        switch(bloodType){
            case "A": bt = BloodType.A; break;
            case "AB": bt = BloodType.AB; break;
            case "B": bt = BloodType.B; break;
            case "O": bt = BloodType.O;
        }


        /* 필기.
         *  회원으로부터 회원가입을 위한 정보를 입력받아 Member 타입 객체 하나로 가공 처리할 방법이 두 가지가 있다.
         *  1. 생성자 방식(장: 한줄 코딩, 단: 따로 생성자 추가 및 생성자의 매개변수가 다소 늘어날 수 있음(리팩토링 징조))
         *  2. setter 방식(장: 초기화 할 갯수 수정 용이, 가독성이 좋음 단: 코드의 줄 수가 늘어남)
        * */
        newMember = new Member(id, pwd, age, hobbies);

        newMember.setBloodType(bt);

        return newMember;
    }

    /* 설명. id와 pwd를 입력받아 반환하는 메소드*/
    private static int chooseMemNo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("회원의 번호를 입력하세요: ");
        return sc.nextInt();
    }
    private static int chooseUpdateMemNo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("수정할 회원의 번호를 입력하세요: ");
        return sc.nextInt();
    }
}
