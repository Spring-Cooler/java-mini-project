package com.blackjack.game.service;

import com.blackjack.game.aggregate.Game;
import com.blackjack.game.aggregate.GameResponseObject;
import com.blackjack.game.repository.GameRepository;
import com.blackjack.member.service.MemberService;

import java.util.ArrayList;

public class GameService {

    private final GameRepository gameRepository;

    public GameService(MemberService memberService) {
        gameRepository = new GameRepository(memberService);
    }

    public GameResponseObject saveGame(Game game) {
        if(game == null) return new GameResponseObject(null, false);
        ArrayList<Game> gameList = new ArrayList<>();
        gameList.add(game);
        return new GameResponseObject(gameList, gameRepository.insertGame(game));
    }

    public GameResponseObject findGamesByMemNo(int memNo) {
        ArrayList<Game> gameList = gameRepository.selectGamesByMemNo(memNo);
        if(gameList == null) return new GameResponseObject(null, false);
        else return new GameResponseObject(gameList,true);
    }

    public GameResponseObject findGamesByNickname(String nickname) {
        ArrayList<Game> gameList = gameRepository.selectGamesByNickname(nickname);
        if(gameList == null) return new GameResponseObject(null, false);
        else return new GameResponseObject(gameList,true);
    }

    public GameResponseObject removeGame(int memNo) {
        return new GameResponseObject(null,gameRepository.deleteGameByMemNo(memNo));
    }
}
