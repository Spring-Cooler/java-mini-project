package testapp.aggregate;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Member implements Serializable {
    private int memNo;                  //회원 번호
    private String id;                  // 회원 아이디
    private String pwd;                 // 회원 비번
    private int age;                    // 회원 나이
    private String[] hobbies;           // 회원 취미들
    private BloodType bloodType;        // 회원 혈액형
    public Member() {
    }

    public Member(String id, String pwd, int age, String[] hobbies) {
        this.id = id;
        this.age = age;
        this.hobbies = hobbies;
        StringBuilder sb = null;
        try {
            // MD5 해시 함수 객체 생성
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 해시할 문자열
            String input = pwd;

            // 문자열을 바이트 배열로 변환하여 해시 계산
            byte[] hashInBytes = md.digest(input.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
            sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }

            // 해시 값 출력
            System.out.println("MD5 hash: " + sb.toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.pwd = sb.toString();
    }


    public Member(int memNo, String id, String pwd, int age, String[] hobbies, BloodType bloodType) {
        this.memNo = memNo;
        this.id = id;
        this.pwd = pwd;
        this.age = age;
        this.hobbies = hobbies;
        this.bloodType = bloodType;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memNo=" + memNo +
                ", id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", bloodType=" + bloodType +
                '}';
    }

    public int getMemNo() {
        return memNo;
    }

    public void setMemNo(int memNo) {
        this.memNo = memNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}
