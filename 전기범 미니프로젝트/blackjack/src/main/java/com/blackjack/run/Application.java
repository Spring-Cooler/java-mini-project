package com.blackjack.run;

import com.blackjack.game.aggregate.Card;
import com.blackjack.game.aggregate.Game;
import com.blackjack.game.aggregate.GameResponseObject;
import com.blackjack.game.aggregate.Rank;
import com.blackjack.game.service.GameService;
import com.blackjack.member.aggregate.LoginForm;
import com.blackjack.member.aggregate.Member;
import com.blackjack.member.aggregate.MemberResponseObject;
import com.blackjack.member.aggregate.Tier;
import com.blackjack.member.service.MemberService;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class Application {

    private static final MemberService memberService = new MemberService();
    private static final GameService gameService = new GameService();
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
                    default: System.out.println("\n잘못된 입력입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static void goLounge() {
        Scanner sc = new Scanner(System.in);
//        System.out.println(memNo);
        while (true) {
            System.out.println("\n====== 라운지 ======");
            System.out.println("1. 게임 시작");
            System.out.println("2. 내 정보 보기");
            System.out.println("3. 회원 검색");
            System.out.println("4. 무료 충전");
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
                        MemberResponseObject checkValidPlayer = memberService.findMemberByMemNo(memNo);
                        if(checkValidPlayer.getCheckValueBoolean()) {
                            System.out.println("\n게임을 시작합니다.");
                            try {
                                playGame(checkValidPlayer.getMember());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else System.out.println("\n플레이어 정보를 불러오지 못했습니다.");
                        break;
                    case 2:
                        MemberResponseObject checkMyInfoSuccess = memberService.findMemberByMemNo(memNo);
                        GameResponseObject checkMyGameSuccess = gameService.findGamesByMemNo(memNo);
                        if (checkMyInfoSuccess.getCheckValueBoolean()) {
                            printMemInfo(checkMyInfoSuccess.getMember());
                            printGameListInfo(checkMyGameSuccess.getGameList());
                            selectMyInfoMenuNo(checkMyInfoSuccess.getMember(),checkMyGameSuccess.getGameList());
                        }
                        else System.out.println("\n내 정보를 불러오지 못했습니다.");
                        break;
                    case 3:
                        String nickname = searchNickname();
                        MemberResponseObject checkMemInfoSuccess = memberService.findMemberByNickname(nickname);
                        GameResponseObject checkMemGameSuccess =
                                gameService.findGamesByMemNo(checkMemInfoSuccess.getMember().getMemNo());
                        if (checkMemInfoSuccess.getCheckValueBoolean()) {
                            printMemInfo(checkMemInfoSuccess.getMember());
                            printGameListInfo(checkMemGameSuccess.getGameList());
                        } else {
                            System.out.println("\n존재하지 않는 닉네임입니다.");
                        }
                        break;
                    case 4:
                        Member member = memberService.findMemberByMemNo(memNo).getMember();
                        if(member.getDollars() < 100) {
                            member.setDollars(100);
                            memberService.modifyMember(member);
                            System.out.println("\n충전이 완료되었습니다!");
                        } else {
                            System.out.println("\n금액이 이미 충분합니다.");
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
                            gameService.removeGame(memNo);
                            System.out.println("\n회원 탈퇴 완료");
                            return;
                        } else {
                            System.out.println("\n회원 탈퇴 실패");
                            break;
                        }
                    default: System.out.println("\n잘못된 입력입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static void playGame(Member member) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        updateTier(member);
        Game game = new Game(member);
        int betLimit = game.getBetLimit();
        boolean checkPlayAtLeastOnce = false;
        while(true) {
            ArrayList<Card> dealerCard = new ArrayList<>();
            ArrayList<Card> playerCard = new ArrayList<>();
            printGameStatus(member, betLimit, game);
            while(true) {
                System.out.println("배팅할 금액을 정하세요(방 나가기(Q)): ");
                String line = sc.nextLine();
                if (line.equals("Q") | line.equals("q"))  {
                    if(checkPlayAtLeastOnce) gameService.saveGame(game);
                    updateTier(member);
                    System.out.println("\n====== 최종 결과 ======");
                    System.out.println("손익: " + game.getResult() + "달러($)");
                    System.out.println("\n방을 나갑니다.");
                    return;
                }
                try {
                    int input = Integer.parseInt(line);
                    if (input > member.getDollars())
                        System.out.println("\n배팅 금액이 가지고 있는 돈보다 많습니다.\n");
                    else if(input > betLimit)
                        System.out.println("\n이 방의 베팅 최대치는 $" + betLimit + "입니다.\n");
                    else if(input < 2)
                        System.out.println("\n최소 2달러($)이상 베팅해야합니다.\n");
                    else {
                        game.bet(input);
//                        System.out.println(member);
                        memberService.modifyMember(member);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n잘못된 입력입니다.\n");
                }
            }

            Thread.sleep(500);

            printGameStatus(member, betLimit, game);
            Card playerCard1 = game.getDeck().dealCard();
            Card playerCard2 = game.getDeck().dealCard();
            playerCard.add(playerCard1);
            playerCard.add(playerCard2);

            Card dealerCard1 = game.getDeck().dealCard();
            Card dealerCard2 = game.getDeck().dealCard();
            dealerCard.add(dealerCard1);
            dealerCard.add(dealerCard2);

            printBothCards(dealerCard, playerCard,true);

            boolean checkPlayerBlackjack = game.isBlackjack(playerCard1,playerCard2);
            boolean checkPlayerInsurance = false;
            boolean skipFlag = false;

            if(checkPlayerBlackjack) System.out.println("\n플레이어 블랙잭! 축하드립니다~\n");

            // 딜러의 두번째 카드가 에이스인 경우 인슈어런스 및 이븐 머니 선택 제공
            if ((dealerCard2.getRank() == Rank.ACE) && (member.getDollars() >= game.getBet()/2)) {
                if(!checkPlayerBlackjack) {
                    System.out.println("인슈어런스? (Y/N)");
                    // 입력을 통해 인슈어런스 결정
                    String decisionInsurance = sc.nextLine();
                    if (decisionInsurance.equals("Y") | decisionInsurance.equals("y")) {
                        game.placeInsurance();
//                    System.out.println(member);
                        memberService.modifyMember(member);
                        checkPlayerInsurance = true;
                    }
                } else {
                    System.out.println("이븐 머니? (Y/N)");
                    String decisionEven = sc.nextLine();
                    if (decisionEven.equals("Y") | decisionEven.equals("y")) {
                        game.setEvenMoney(true);
                        game.evenMoney();
                        skipFlag = true;
                        System.out.println("\n이븐 머니");
                        System.out.println("플레이어 Win");
                    }
                }
            }

            if(!skipFlag) {
                // 딜러 블랙잭 확인
                if (game.isBlackjack(dealerCard1, dealerCard2)) {
                    Thread.sleep(500);
                    printGameStatus(member, betLimit, game);
                    printBothCards(dealerCard, playerCard, false);
                    System.out.println("\n딜러 블랙잭!");
                    if(checkPlayerInsurance) game.insurance(true); // 인슈어런스 처리
//                System.out.println(member);
                    memberService.modifyMember(member);

                    if (checkPlayerBlackjack) {
                        game.push();
//                    System.out.println(member);
                        System.out.println("\n푸시");
                    } else {
                        game.dealerWin();
//                    System.out.println(member);
                        System.out.println("딜러 Win");
                    }
                    memberService.modifyMember(member);
                } else {
                    if (checkPlayerInsurance) {
                        System.out.println("\nNO 블랙잭");
                        game.insurance(false); // 인슈어런스 처리
//                    System.out.println(member);
                        memberService.modifyMember(member);
                    }

                    skipFlag = true;
                    boolean checkPlayerStand = false;
                    boolean checkPlayerBust = false;
                    if (!checkPlayerBlackjack) {
                        while (true) {
                            if (!skipFlag) {
                                Thread.sleep(500);
                                printGameStatus(member, betLimit, game);
                                printBothCards(dealerCard, playerCard, true);
                            }
                            if (Card.sumCardsPoint(playerCard, false) > 21) {
                                checkPlayerBust = true;
                                game.dealerWin();
//                            System.out.println(member);
                                memberService.modifyMember(member);
                                System.out.println("\n플레이어 버스트");
                                System.out.println("딜러 Win");
                                break;
                            }

                            System.out.println("\n힛 OR 스탠드? (1 OR 2)");
                            String line = sc.nextLine();
                            try {
                                int input = Integer.parseInt(line);
                                switch (input) {
                                    case 1:
                                        playerCard.add(game.getDeck().dealCard());
                                        break;
                                    case 2:
                                        checkPlayerStand = true;
                                        break;
                                    default:
                                        System.out.println("\n잘못된 입력입니다.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("\n잘못된 입력입니다.");
                            }

                            if (checkPlayerStand) break;
                            skipFlag = false;
                        }
                    }

                    if (!checkPlayerBust) {
                        boolean delayFlag = false;
                        int playerPoints = Card.sumCardsPoint(playerCard, false);
                        while (true) {
                            if (delayFlag) Thread.sleep(1500);
                            else Thread.sleep(500);
                            printGameStatus(member, betLimit, game);
                            printBothCards(dealerCard, playerCard, false);
                            int dealerPoints = Card.sumCardsPoint(dealerCard, false);
                            if (dealerPoints > 21) {
                                if (checkPlayerBlackjack) game.blackjack();
                                else game.playerWin();
//                            System.out.println(member);
                                memberService.modifyMember(member);
                                System.out.println("\n딜러 버스트");
                                System.out.println("플레이어 Win");
                                break;
                            }
                            if (dealerPoints >= 17) {
                                if (dealerPoints > playerPoints) {
                                    game.dealerWin();
//                                System.out.println(member);
                                    memberService.modifyMember(member);
                                    System.out.println("\n딜러 Win");
                                    break;
                                } else if (dealerPoints == playerPoints) {
                                    game.push();
//                                System.out.println(member);
                                    memberService.modifyMember(member);
                                    System.out.println("\n푸시");
                                    break;
                                } else dealerCard.add(game.getDeck().dealCard());
                            } else dealerCard.add(game.getDeck().dealCard());

                            if (!delayFlag) delayFlag = true;
                        }
                    }
                }
            }

            Thread.sleep(500);
            printGameStatus(member, betLimit, game);
            if(member.getDollars() == 0) {
                gameService.saveGame(game);
                updateTier(member);
                System.out.println("\n====== 최종 결과 ======");
                System.out.println("손익: " + game.getResult() + "달러($)");
                System.out.println("\n방을 나갑니다.");
                return;
            }

            while(true) {
                System.out.println("\n다시하시겠습니까? (Y/N)");
                String decisionOneMoreGame = sc.nextLine();
                if (decisionOneMoreGame.equals("N") | decisionOneMoreGame.equals("n")) {
                    gameService.saveGame(game);
                    updateTier(member);
                    System.out.println("\n====== 최종 결과 ======");
                    System.out.println("손익: " + game.getResult() + "달러($)");
                    System.out.println("\n방을 나갑니다.");
                    return;
                }
                else if (decisionOneMoreGame.equals("Y") | decisionOneMoreGame.equals("y")) {
                    checkPlayAtLeastOnce = true;
                    break;
                }
                else System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static void updateTier(Member member) {
        int dollars = member.getDollars();
        if(dollars >= 1000000) member.setTier(Tier.DIAMOND);
        else if(dollars >= 100000) member.setTier(Tier.PLATINUM);
        else if(dollars >= 10000) member.setTier(Tier.GOLD);
        else if(dollars >= 1000) member.setTier(Tier.SILVER);
        else member.setTier(Tier.BRONZE);
        memberService.modifyMember(member);
    }

    private static void printGameStatus(Member member, int betLimit, Game game) {
        System.out.println("\n====== Black Jack ======");
        System.out.println("현재 티어: " + member.getTier());
        System.out.println("잔고: $" + member.getDollars());
        System.out.println("최대 베팅: $" + betLimit);
        System.out.println("현재 베팅: $" + game.getBet());
        System.out.println("현재 손익: " + game.getResult() + "달러($)");
    }

    private static void printBothCards(ArrayList<Card> dealerCard, ArrayList<Card> playerCard, boolean isPlayerTurn) {
        System.out.println("\n====== 딜러 카드 ======");
        Card.printHorizontalCards(dealerCard, isPlayerTurn);
        System.out.println("======================");
        System.out.println("딜러 숫자 합: " + Card.sumCardsPoint(dealerCard,isPlayerTurn));

        System.out.println("\n====== 플레이어 카드 ======");
        Card.printHorizontalCards(playerCard, false);
        System.out.println("======================");
        System.out.println("플레이어 숫자 합: " + Card.sumCardsPoint(playerCard,false));
    }

    private static void selectMyInfoMenuNo(Member member, ArrayList<Game> gameList) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("\n1. 정보 수정");
            System.out.println("9. 뒤로 가기");
            System.out.println("번호를 입력하세요: ");
            String line = sc.nextLine();
            if(line.length() > 1) {
                System.out.println("\n잘못된 입력입니다.");
                continue;
            }
            try {
                int input = Integer.parseInt(line);
                switch (input) {
                    case 1:
                        MemberResponseObject checkModifySuccess = memberService.modifyMember(changeMyInfo(member));
                        if (!checkModifySuccess.getCheckValueBoolean()) System.out.println("\n정보 수정 실패");
                        else System.out.println("\n정보 수정 완료");
                        printMemInfo(checkModifySuccess.getMember());
                        printGameListInfo(gameList);
                        break;
                    case 9:
                        return;
                    default: System.out.println("\n잘못된 입력입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.");
            }
        }
    }

    private static Member changeMyInfo(Member member) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n====== 내 정보 수정 ======");
            System.out.println("1. 닉네임 변경");
            System.out.println("2. 비밀번호 변경");
            System.out.println("9. 저장하기");
            System.out.println("========================");
            System.out.println("번호를 입력하세요: ");
            String line = sc.nextLine();
            if(line.length() > 1) {
                System.out.println("\n잘못된 입력입니다.");
                continue;
            }
            try {
                int input = Integer.parseInt(line);
                switch (input) {
                    case 1:
                        System.out.println("새 닉네임: ");
                        String newNickname = sc.nextLine();
                        MemberResponseObject checkValidNickname = memberService.findMemberByNickname(newNickname);
                        if(checkValidNickname.getCheckValueBoolean()) System.out.println("\n중복된 닉네임입니다.");
                        else {
                            System.out.println("\n사용 가능한 닉네임입니다.");
                            System.out.println("사용하시겠습니까?(Y/N): ");
                            String YOrN = sc.nextLine();
                            if(YOrN.equals("Y") | YOrN.equals("y")) member.setNickname(newNickname);
                            else if(YOrN.equals("N") | YOrN.equals("n")) break;
                            else System.out.println("\n잘못된 입력입니다.");
                        }
                        break;
                    case 2:
                        System.out.println("새 비밀번호: ");
                        String newPwd = sc.nextLine();
                        System.out.println("새 비밀번호 확인");
                        String newPwdConfirm = sc.nextLine();
                        if(newPwd.equals(member.getPwd())) {
                            System.out.println("\n기존 비밀번호와 동일합니다.");
                            break;
                        }
                        if(!newPwd.equals(newPwdConfirm)) System.out.println("\n비밀번호가 서로 일치하지 않습니다.");
                        else member.setPwd(newPwd);
                        break;
                    case 9:
                        System.out.println("\n저장 완료");
                        return member;
                    default: System.out.println("\n잘못된 입력입니다.");
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

    private static void printMemInfo(Member member) {
        System.out.println("\n====== 회원 정보 ======");
//        System.out.println("회원번호: " + mro.getMember().getMemNo());
        System.out.println("닉네임: " + member.getNickname());
        System.out.println("티어: " + member.getTier());
        System.out.println("나이: " + member.getAge());
        System.out.println("잔고: $" + member.getDollars());
        System.out.println("====================");
    }

    private static void printGameListInfo(ArrayList<Game> gameList) {
        System.out.println("\n====== 최근 게임 ======");
        if(gameList.isEmpty()) System.out.println("최근 전적이 없습니다.");
        for(Game game: gameList) {
            System.out.println(game);
        }
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

        flag = true;
        String line;
        int memAge = 0;
        do {
            System.out.println("나이: ");
            line = sc.nextLine();
            try {
                memAge = Integer.parseInt(line);
                if(memAge < 1 || memAge > 130) {
                    System.out.println("\n잘못된 입력입니다.\n");
                    continue;
                }
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("\n잘못된 입력입니다.\n");
            }
        } while(flag);
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
