package com.blackjack.member.aggregate;

import java.io.Serializable;
import java.util.Objects;

public class Member implements Serializable, Comparable<Member> {
    private int memNo;
    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private int age;
    private int dollars = 100;

    public Member() {
    }

    public Member(String id, String pwd, String name, String nickname, int age) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.nickname = nickname;
        this.age = age;
    }

    public Member(int memNo, String id, String pwd, String name, String nickname, int age) {
        this(id, pwd, name, nickname, age);
        this.memNo = memNo;
    }

    public Member(Member member) {
        this.memNo = member.memNo;
        this.id = member.id;
        this.pwd = member.pwd;
        this.name = member.name;
        this.nickname = member.nickname;
        this.age = member.age;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return memNo == member.memNo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memNo);
    }

    @Override
    public String toString() {
        return "Member{" +
                "memNo=" + memNo +
                ", id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", dollars=" + dollars +
                '}';
    }

    @Override
    public int compareTo(Member o) {
        return this.memNo - o.memNo;
    }
}
