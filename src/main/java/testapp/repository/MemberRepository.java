package testapp.repository;

import testapp.aggregate.BloodType;
import testapp.aggregate.Member;
import testapp.stream.MyObjectOutput;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

/* 설명. 데이터와 입출력(CRUD)을 위해 만들어지며 성공 또는 실패 여부를 반환 */
public class MemberRepository {

    private ArrayList<Member> memberList = new ArrayList<>();

    /* 설명.
     *  최초에 생성될 객체의 생성자
     *  1. 초기 회원 세명을 넣어둘 예정 (스트림을 활용한 객체 입력)
     *  2. 파일로부터 회원 정보를 가져온다.(스트림을 활용한 객체 출력)
     *  3. ArrayList를 활용해 가져온 회원 정보를 저장
     * */
    public MemberRepository() {
        File file = new File("src/main/java/testapp/db/memberDB.dat");

        /* 설명. main을 실행할 때마다 덮어 씌우는 문제르 방지하고자 파일이없을 때만 초기 세티잉 되도록 조건문 작성 */
        if (!file.exists()) {
            ArrayList<Member> defaultMembers = new ArrayList<>();        // 초기 회원 세명만 담는 용도의 ArrayList(지역변수)
            defaultMembers.add(new Member(1, "user01", toMD5("pass01"), 20
                    , new String[]{"발레", "수영"}, BloodType.A));
            defaultMembers.add(new Member(2, "user02", toMD5("pass02"), 10,
                    new String[]{"게임", "영화시청"}, BloodType.B));
            defaultMembers.add(new Member(3, "user03", toMD5("pass03"), 15
                    , new String[]{"음악감상", "독서", "명상"}, BloodType.O));

            /* 설명. 초기 멤버 3명을 파일로 객체 출력하는 코드 작성 */
            saveMembers(file, defaultMembers);
        }

        /* 설명. 파일로부터 회원객체들을 입력받아 memberList에 쌓기 */
        loadMember(file);

        /* 설명. loadMember() 메소드를 통해 잘 불러와졌는지 확인 */
//        for(Member member: memberList){
//            System.out.println("member = "+member);
//        }
    }

    private String toMD5(String pwd) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(pwd.getBytes());
            // 바이트 배열을 16진수 문자열로 변환
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private void loadMember(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(file)
                    )
            );
            while (true) {
                memberList.add((Member) ois.readObject());
            }
        } catch (EOFException e) {
            System.out.println("회원 정보 모두 로딩됨...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void saveMembers(File file, ArrayList<Member> members) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)
                    )
            );

            /* 설명. 초기 멤버 3명을 각각 객체 출력 내보내기 */
            for (Member member : members) {
                oos.writeObject(member);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (oos != null) oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public ArrayList<Member> selectAllMembers() {
        return memberList;
    }

    public Member selectMemberBy(int memNo) {
        for (Member mem : memberList) {
            if (mem.getMemNo() == memNo) return mem;
        }
        return null;
    }

    public int insertMember(Member newMember) {

        /* 설명. 객체 파일 출력으로 insert */
        MyObjectOutput moo = null;
        int result = 0;
        try {
            moo = new MyObjectOutput(
                    new BufferedOutputStream(
                            new FileOutputStream("src/main/java/testapp/db/memberDB.dat"
                                    , true)
                    )
            );

            /* 설명. 파일로 새로운 회원 객체 출력하기(기존 회원에 추가) */
            moo.writeObject(newMember);

            /* 설명. 파일 출력이 성공하면 회원을 관리하는 컬렉션에도 추가(최신화) */
            memberList.add(newMember);

            /* 설명. 객체 한 개  insert 성공을 의미하는 int 값*/
            result = 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (moo != null) moo.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    /* 설명. insert를 하기 위해 회원 번호를 방생시키는데 사용되는 마지막 회원의 번호 추출 기능 메소드*/
    public int selectLastMemberNo() {
        /* 설명. 1. 마지막 회원을 조회해서 그 회원의 회원번호를 확인(항상 맞지는 X) */
        Member lastMember = memberList.get(memberList.size() - 1);
        return lastMember.getMemNo();

        /* 설명. 2. 컬렉션의 크기가 곧 회원 번호이므로 마지막 회원의 번호는 컬렉션의 크기(항상 맞지는 X)*/
//        return memberList.size();
    }

    public int deleteMember(int removeMember) {
        /* 설명.
         *  현재는 우리가 마지막 회원 번호 추출을 위해 컬렉션의 size()를 활용하고 있다.
         *  (그래서 hard delete(실제 회원 객치를 제거)를 할 수 없고
         *  soft delete(회원 중에 탈퇴와 관련된 속성 수정)를 진행해야 한다.
         *
         * 설명.
         *  hard delete를 하게 된다면 memberList에서 회원 한 명을 remove() 메소드를 활용하여 삭제하고
         *  파일 객체 출력을 통해 파일의 컬렉션에 있는 회원들을 다시 덮어 씌워야 한다.(ObjectOutpurStream을 활용해서)
         * */

//        int result=0;
//            for (Member mem : memberList) {
//                if (mem.getMemNo() == removeMember) {
//                    mem.setId(null);
//                }
//            }
//        
//            result = 1;
//        return result;

        /* 설명. hard delete 시 */
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getMemNo() == removeMember) {
                memberList.remove(i);

                File file = new File("src/main/java/com/ohgiraffers/section04/testapp/db/memberDB.dat");
                saveMembers(file, memberList);

                /* 설명. 컬렉션의 모든 내용을 파일로 출력(덮어씌우기) */
                return 1;
            }
        }
        return 0;
    }

    public int updateMember(int updateMember) {
        Scanner sc =new Scanner(System.in);
        for (Member mem : memberList) {
                if (mem.getMemNo() == updateMember) {
                    System.out.print("수정할 아이디를 입력하세요: ");
                    String id =sc.nextLine();
                    mem.setId(id);
                    System.out.print("수정할 패스워드를 입력하세요: ");
                    String pwd = sc.nextLine();
                    StringBuilder sb = new StringBuilder();
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        String input=pwd;
                        byte[] hashInBytes = md.digest(input.getBytes());
                        // 바이트 배열을 16진수 문자열로 변환
                        for (byte b : hashInBytes) {
                            sb.append(String.format("%02x", b));
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    mem.setPwd(sb.toString());
                    System.out.print("수정할 나이를 입력하세요: ");
                    int age = sc.nextInt();
                    mem.setAge(age);
                    sc.nextLine();
                    System.out.println("수정할 취미를 입력하세요: ");
                    String[] hobbies =mem.getHobbies();
                    for(int i=0;i<mem.getHobbies().length;i++){
                        System.out.println((i+1)+"번째 취미를 입력하세요: ");
                        String input = sc.nextLine();
                        hobbies[i]=input;
                    }
                    System.out.println("수정할혈액형을 입력하세요(A, AB, B, O): ");
                    String bloodType = sc.nextLine().toUpperCase();
                    BloodType bt = null;
                }
            }
        return 1;
    }

    public int login() {
        int result=0;
        String memberpwd;
        System.out.println("====로그인====");
        Scanner sc =new Scanner(System.in);
        System.out.print("아이디를 입력하세요: ");
        String id =sc.next();
        System.out.print("패스워드를 입력하세요: ");
        String pwd = sc.next();
        StringBuilder sb1 = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String input=pwd;
            byte[] hashInBytes = md.digest(input.getBytes());
            for (byte b : hashInBytes) {
                sb1.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        for(Member mem:memberList){
            if(mem.getId().equals(id)){
                memberpwd =mem.getPwd();
                result=1;
                if(memberpwd.equals(sb1.toString())){
                    result= 2;
                }
            }
        }
        return result;
    }
}
