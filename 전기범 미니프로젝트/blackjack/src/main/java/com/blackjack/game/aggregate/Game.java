package com.blackjack.game.aggregate;

import com.blackjack.member.aggregate.Member;
import com.blackjack.member.aggregate.Tier;

public class Game {
    private Deck deck;
    private Member player;
    private int betLimit = 10;
    private int bet = 0;
    private int insuranceBet = 0;
    private boolean evenMoney = false;
    private int result = 0;

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
        this.bet = 0;
    }

    public void placeInsurance() {
        if (this.bet > 0 && this.insuranceBet == 0) {
            this.insuranceBet = bet / 2; // 인슈어런스는 원래 베팅 금액의 절반
            player.setDollars(player.getDollars() - this.insuranceBet);
        } else {
            System.out.println("\n인슈어런스를 할 수 없습니다.");
        }
    }

    public boolean getEvenMoney() {
        return evenMoney;
    }

    public void setEvenMoney(boolean evenMoney) {
        this.evenMoney = evenMoney;
    }

    public boolean isBlackjack(Card Card1, Card Card2) {
        return  (Card1.getRank() == Card.Rank.ACE &&
                    (Card2.getRank() == Card.Rank.TEN || Card2.getRank() == Card.Rank.JACK ||
                     Card2.getRank() == Card.Rank.QUEEN || Card2.getRank() == Card.Rank.KING)) ||
                (Card2.getRank() == Card.Rank.ACE &&
                     (Card1.getRank() == Card.Rank.TEN || Card1.getRank() == Card.Rank.JACK ||
                      Card1.getRank() == Card.Rank.QUEEN || Card1.getRank() == Card.Rank.KING));
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
