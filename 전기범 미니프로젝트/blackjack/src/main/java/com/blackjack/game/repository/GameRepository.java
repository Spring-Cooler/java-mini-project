package com.blackjack.game.repository;

import com.blackjack.game.aggregate.Game;
import com.blackjack.member.aggregate.Member;
import com.blackjack.stream.MyObjectOutput;

import java.io.*;
import java.util.ArrayList;

public class GameRepository {

    private ArrayList<Game> gameList = new ArrayList<>();
    private final String filePath = "전기범 미니프로젝트/blackjack/src/main/java/com/blackjack/game/db/gameDB.dat";
    private final File file;

    public GameRepository() {
        file = new File(filePath);
        if(!file.exists()) {
            saveGames(file, gameList);
        }
        loadGames(file);
    }

    private void saveGames(File file, ArrayList<Game> games) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)
                    )
            );

            for(Game game: games) {
                oos.writeObject(game);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(oos != null) oos.close();
            } catch (IOException e) {
                System.out.println("oos close failure");
            }
        }
    }

    private void loadGames(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(file)
                    )
            );

            while(true) {
                gameList.add((Game)ois.readObject());
            }
        } catch (EOFException e) {
            System.out.print("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(ois != null) ois.close();
            } catch (IOException e) {
                System.out.println("ois close failure");
            }
        }
    }

    private int selectLastGameNo() {
        if(gameList.isEmpty()) return 0;
        return gameList.get(gameList.size() - 1).getGameNo();
    }

    public boolean insertGame(Game game) {
        MyObjectOutput moo = null;
        boolean result = false;
        try {
            moo = new MyObjectOutput(
                    new BufferedOutputStream(
                            new FileOutputStream(filePath, true)
                    )
            );
            game.setGameNo(selectLastGameNo()+1);
//            System.out.println(member);
            moo.writeObject(game);

            gameList.add(game);

            result = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(moo != null) moo.close();
            } catch (IOException e) {
                System.out.println("moo close failure");
            }
        }
        return result;
    }

    public ArrayList<Game> selectGamesByMemNo(int memNo) {
        ArrayList<Game> games = new ArrayList<>();
        int cnt = 0;
        for(int i=gameList.size()-1; i>=0; i--) {
            Game game = gameList.get(i);
            if(game.getPlayer().getMemNo() == memNo) {
                games.add(game);
                cnt++;
            }
            if(cnt == 10) break;
        }
        return games;
    }

    public boolean deleteGameByMemNo(int memNo) {
        ArrayList<Game> newGameList = new ArrayList<>();
        for(Game game: gameList) {
            Member member = game.getPlayer();
            if(member.getMemNo() == memNo) continue;
            newGameList.add(game);
        }
        gameList = newGameList;
        saveGames(file,gameList);
        return true;
    }
}
