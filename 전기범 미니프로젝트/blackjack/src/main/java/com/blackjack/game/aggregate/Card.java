package com.blackjack.game.aggregate;

import java.util.ArrayList;

public class Card {

    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public static int sumCardsPoint(ArrayList<Card> playerCard, boolean isDealerCardAndFirstRound) {
        int sum = 0;
        boolean checkAce = false;
        for(Card card : playerCard) {
            if(isDealerCardAndFirstRound) {
                isDealerCardAndFirstRound = false;
                continue;
            }
            Rank rank = card.getRank();
            if(rank == Card.Rank.ACE) {
                checkAce = true;
                sum+=11;
            }
            else if(rank == Card.Rank.TEN || rank == Card.Rank.JACK ||
                    rank == Card.Rank.QUEEN || rank == Card.Rank.KING) {
                sum+=10;
            }
            else {
                sum+=rank.ordinal()+2;
            }
        }
        if(checkAce && sum > 21) sum-=10;
        return sum;
    }

    public static void printHorizontalCards(ArrayList<Card> cards, boolean isDealerCardAndFirstRound) {
        // 각 줄을 생성하여 합친다
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();
        StringBuilder line6 = new StringBuilder();
        StringBuilder line7 = new StringBuilder();

        for (Card card : cards) {
            String[] cardLines;
            if(isDealerCardAndFirstRound) {
                cardLines = card.getBackSide().split("\n");
                isDealerCardAndFirstRound = false;
            }
            else cardLines = card.getFrontSide().split("\n");
            line1.append(cardLines[0]).append(" ");
            line2.append(cardLines[1]).append(" ");
            line3.append(cardLines[2]).append(" ");
            line4.append(cardLines[3]).append(" ");
            line5.append(cardLines[4]).append(" ");
            line6.append(cardLines[5]).append(" ");
            line7.append(cardLines[6]).append(" ");
        }

        // 출력
        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
        System.out.println(line6);
        System.out.println(line7);
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    private String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case SPADES -> "♠";
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
        };
    }

    private String getRankDisplay(Rank rank) {
        return switch (rank) {
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
            case ACE -> "A";
        };
    }

    public String getBackSide() {
        return "┌─────────┐\n" +
               "│░░░░░░░░░│\n" +
               "│░░░░░░░░░│\n" +
               "│░░░░░░░░░│\n" +
               "│░░░░░░░░░│\n" +
               "│░░░░░░░░░│\n" +
               "└─────────┘";
    }

    public String getFrontSide() {
        String suitSymbol = getSuitSymbol(suit);
        String rankDisplay = getRankDisplay(rank);

        // 카드 출력 모양

        return  "┌─────────┐\n" +
                "│ " + rankDisplay + "       │\n" +
                "│         │\n" +
                "│    " + suitSymbol + "    │\n" +
                "│         │\n" +
                "│       " + rankDisplay + " │\n" +
                "└─────────┘";
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean isFaceDown) {
        if (isFaceDown) {
            return getBackSide();
        } else {
            return getFrontSide();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.suit == other.suit && this.rank == other.rank;
    }

    @Override
    public int hashCode() {
        return suit.hashCode() * 31 + rank.hashCode();
    }
}
