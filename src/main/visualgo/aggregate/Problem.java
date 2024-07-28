package src.main.visualgo.aggregate;

import src.main.visualgo.aggregate.Tier;
import java.io.File;
import java.util.ArrayList;

public record Problem() {
    static int testCount;
    static String index; // name ex) baekjoon-1000
    static String url; // ex) boj/problems/1661
    static ArrayList<main.visualgo.aggregate.Algorithms> algorithms; // ex) spanningTree, graph, dfs, bfs, ...
    static Tier tier; // ex BaekjoonGold, Programmer1
    static String notes; // annotations
    static ArrayList<File> solutions;
    static ArrayList<File> testCases; //
    static ArrayList<File> results;//
}
