package com.blackjack.game.aggregate;

import com.blackjack.member.aggregate.Member;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {

    private int gameNo;
    private Member player;
    private int result = 0;
    private transient Deck deck;
    private transient int betLimit = 10;
    private transient int bet = 0;
    private transient int insuranceBet = 0;
    private transient boolean evenMoney = false;

    public Game(Member player) {
        this.deck = new Deck();
        deck.shuffle();
        this.player = player;
        switch (player.getTier()) {
            case BRONZE: this.betLimit = 10; break;
            case SILVER: this.betLimit = 100; break;
            case GOLD: this.betLimit = 1000; break;
            case PLATINUM: this.betLimit = 10000; break;
            case DIAMOND: this.betLimit = 100000; break;
        }
    }

    public void bet(int dollars) {
        player.setDollars(player.getDollars() - dollars);
        this.bet += dollars;
    }

    public void blackjack() {
        player.setDollars(player.getDollars() + this.bet/2);
        this.result += (this.bet/2);
        playerWin();
    }

    public void evenMoney() {
        playerWin();
        this.evenMoney = false;
    }

    public void insurance(boolean checkDealerBlackjack) {
        if (insuranceBet > 0) {
            if(checkDealerBlackjack) {
                player.setDollars(player.getDollars() + 2 * insuranceBet); // 인슈어런스 베팅 금액을 2배로 돌려받음
                this.result += 2 * insuranceBet;
            }
            else this.result -= insuranceBet;
            insuranceBet = 0;
        }
    }

    public void playerWin() {
        player.setDollars(player.getDollars() + 2*this.bet);
        this.result += this.bet;
        this.bet = 0;
    }

    public void dealerWin() {
        this.result -= this.bet;
        this.bet = 0;
    }

    public void push() {
        player.setDollars(player.getDollars() + this.bet);
        this.bet = 0;
    }

    public void placeInsurance() {
        if (this.bet > 0 && this.insuranceBet == 0) {
            this.insuranceBet = bet / 2; // 인슈어런스는 베팅 금액의 절반
            player.setDollars(player.getDollars() - this.insuranceBet);
        } else {
            System.out.println("\n인슈어런스를 할 수 없습니다.");
        }
    }

    public boolean isBlackjack(Card Card1, Card Card2) {
        return  (Card1.getRank() == Rank.ACE &&
                    (Card2.getRank() == Rank.TEN || Card2.getRank() == Rank.JACK ||
                     Card2.getRank() == Rank.QUEEN || Card2.getRank() == Rank.KING)) ||
                (Card2.getRank() == Rank.ACE &&
                     (Card1.getRank() == Rank.TEN || Card1.getRank() == Rank.JACK ||
                      Card1.getRank() == Rank.QUEEN || Card1.getRank() == Rank.KING));
    }

    public int getGameNo() {
        return gameNo;
    }

    public void setGameNo(int gameNo) {
        this.gameNo = gameNo;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Member getPlayer() {
        return player;
    }

    public void setPlayer(Member player) {
        this.player = player;
    }

    public int getBetLimit() {
        return betLimit;
    }

    public void setBetLimit(int betLimit) {
        this.betLimit = betLimit;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getInsuranceBet() {
        return insuranceBet;
    }

    public void setInsuranceBet(int insuranceBet) {
        this.insuranceBet = insuranceBet;
    }

    public boolean getEvenMoney() {
        return evenMoney;
    }

    public void setEvenMoney(boolean evenMoney) {
        this.evenMoney = evenMoney;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "게임 [ 티어: " + player.getTier() +
               ", 닉네임: " + player.getNickname() +
               ", 손익: " + result + "달러($) "+
               ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameNo == game.gameNo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameNo);
    }
}
