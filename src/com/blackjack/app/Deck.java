package com.blackjack.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards; //cards というリスト（List） を作成し、デッキのカードを管理する。
    						  //List<Card> の Card は、トランプの1枚を表すクラス。
    public Deck() {
        resetDeck();
    }

    public void resetDeck() { //「void」は、メソッドが「戻り値を返さない」ことの意　「メッセージ表示、状態変更、処理だけしたいとき」
        //String[] suits = {"♠", "♥", "♦", "♣"}; // スート（絵柄）
    	String[] suits = {"spades", "hearts", "diamonds", "clubs"}; // スート（絵柄）
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; // ランク（数字）

        cards = new ArrayList<>(); //空のカードリストを作成。

        // 52枚のカードを作成
        for (String suit : suits) { // ← このループを忘れずに！
        	for (int i = 0; i < ranks.length; i++) {
        		int value;
        		if (ranks[i].equals("A")) {
        			value = 11; // Aは初期値11として扱う（あとで超えたら1にする処理あり）
        		} else if (i >= 10) {
        			value = 10; // J, Q, K
        		} else {
        			value = i + 1; // 2〜10
        		}
        		cards.add(new Card(suit, ranks[i], value));
        	}
        }

        Collections.shuffle(cards); // デッキをシャッフル
    }

    public Card drawCard() {
    	if (cards.isEmpty()) {
            System.out.println("デッキが空です。新しくシャッフルします。");
            resetDeck(); // ← これで実際にデッキを再生成
        }
        return cards.remove(0); // 山札の一番上のカードを取り出す
    }
}
