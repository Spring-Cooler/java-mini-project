package com.blackjack.domain.game.aggregate;

import java.util.ArrayList;

public class Dealer {
    private ArrayList<Card> dealerCard = new ArrayList<>();

    public void initDealerCard(Deck deck) {
        dealerCard.clear();
        dealerCard.add(deck.dealCard());
        dealerCard.add(deck.dealCard());
    }

    public ArrayList<Card> getDealerCard() {
        return dealerCard;
    }

    public void setDealerCard(ArrayList<Card> dealerCard) {
        this.dealerCard = dealerCard;
    }
}
