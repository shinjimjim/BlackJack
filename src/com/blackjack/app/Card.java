package com.blackjack.app;
//「トランプのカード1枚」を表すシンプルなクラス
public class Card {
    private String suit; // カードのスート（♠, ♥, ♦, ♣）
    private String rank; // カードのランク（A, 2, 3, ..., 10, J, Q, K）
    private int value;   // カードのゲーム内での数値（1～11）

    // コンストラクタ（カードを作るときに呼ばれる）
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }
    
    // ランクを返すメソッド
    public String getRank() {
    	return rank.trim(); // 余分な空白を除去
    }

    // スートを返すメソッド
    public String getSuit() {
        return suit;
    }

    // カードの数値を取得するメソッド
    public int getValue() {
        return value;
    }
    
    // カードの文字列表示（例: "A of spades"）
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
/*
    // カードの情報を文字列として返す（例: "♠A"）
    @Override
    public String toString() {
        return suit + rank;
    }
*/
}
