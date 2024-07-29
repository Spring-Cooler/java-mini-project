package com.blackjack.domain.game.aggregate;

import com.blackjack.domain.member.aggregate.Member;

import java.io.Serializable;
import java.util.ArrayList;

public class Player extends Member implements Serializable {
    private transient ArrayList<Card> playerCard = new ArrayList<>();
    private transient boolean blackjack = false;
    private transient boolean insurance = false;
    private transient boolean stand = false;
    private transient boolean bust = false;
    private transient boolean doubleDown = false;
    private transient boolean surrender = false;
    private transient boolean hit = false;

    public Player(Member member) {
        super(member);
    }

    public void initPlayerCard(Deck deck) {
        playerCard.clear();
        playerCard.add(deck.dealCard());
        playerCard.add(deck.dealCard());
    }

    public void initPlayerStatus() {

        this.setInsurance(false);
        this.setBlackjack(false);
        this.setStand(false);
        this.setBust(false);
        this.setDoubleDown(false);
        this.setSurrender(false);
    }

    public ArrayList<Card> getPlayerCard() {
        return playerCard;
    }

    public void setPlayerCard(ArrayList<Card> playerCard) {
        this.playerCard = playerCard;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }

    public boolean isStand() {
        return stand;
    }

    public void setStand(boolean stand) {
        this.stand = stand;
    }

    public boolean isBust() {
        return bust;
    }

    public void setBust(boolean bust) {
        this.bust = bust;
    }

    public boolean isDoubleDown() {
        return doubleDown;
    }

    public void setDoubleDown(boolean doubleDown) {
        this.doubleDown = doubleDown;
    }

    public boolean isSurrender() {
        return surrender;
    }

    public void setSurrender(boolean surrender) {
        this.surrender = surrender;
    }
}
