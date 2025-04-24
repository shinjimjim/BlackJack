package com.blackjack.app;
//プレイヤーがどうやってカードを持ち、合計値を計算し、手札を表示するか
import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand = new ArrayList<>(); //プレイヤーの「手札」を保持するリスト。
    											 //Card オブジェクト（1枚のカード）を複数保持するために List<Card> を使ってる。
    											 //ArrayList は可変長で、何枚でもカードを追加できる。
    private List<Card> splitHand = new ArrayList<>();
    private boolean hasSplit = false;
    private boolean playingSplit = false;
    
    public void addCard(Card card) { //プレイヤーが新しいカードを受け取る処理。
    	if (hasSplit && playingSplit) { //hasSplit:プレイヤーがスプリットしたかどうかを表すブール値。
    									//playingSplit:今スプリット側の手札を操作しているかを示すフラグ。
            splitHand.add(card); //スプリットした2枚目の手札（splitHand）にカードを追加する。
        } else {
        	hand.add(card); //Deck から引いた Card を hand に追加する。
        }
    }
    
    //ブラックジャックの手札の合計値を正しく計算するロジック
    public int getHandValue() {
        return calculateHandValue(hand); //プレイヤーのメインの手札の合計点数を返します。
    }

    public int getSplitHandValue() {
        return calculateHandValue(splitHand); //スプリットされた分割手札（splitHand）の合計点数を返します。
    }

    private int calculateHandValue(List<Card> targetHand) {
        int total = 0;
        int aceCount = 0;
        for (Card card : targetHand) {
            total += card.getValue(); // カードの基本点数を加算
            if (card.toString().contains("A")) aceCount++; // エースが含まれていたらカウント
        }
        while (total > 21 && aceCount > 0) {
            total -= 10; // エースを11点から1点に変更
            aceCount--;
        }
        return total;
    }

/*    
    public void showHand() {
        System.out.print("メイン: ");
        for (Card card : hand) {
            System.out.print(card + " ");
        }
        System.out.println("(合計: " + getHandValue() + ")");
        
        if (hasSplit) {
            System.out.print("スプリット: ");
            for (Card card : splitHand) {
                System.out.print(card + " ");
            }
            System.out.println("(合計: " + getSplitHandValue() + ")");
        }
    }
*/
    public List<Card> getHand() { //手札リストを外部から取得するgetterメソッド
        return hand;
    }

    public List<Card> getSplitHand() { //手札リストを外部から取得するgetterメソッド
        return splitHand;
    }

    //スプリットできるかどうかを判定
    public boolean canSplit() {
        return hand.size() == 2 &&
               hand.get(0).getRank().equals(hand.get(1).getRank());
    }

    public void split() {
        if (canSplit()) { //canSplit() が true の時だけ処理
            Card second = hand.remove(1); // 2枚目をメイン手札から取り除く
            splitHand.clear(); // splitHand を初期化（念のため）
            splitHand.add(second); // 2枚目を splitHand に追加
            hasSplit = true; // スプリットフラグを立てる
            playingSplit = false; // 最初はメイン手札からスタート
        }
    }
    //状態を確認するメソッド　スプリットしてるか or 今どっちの手札をプレイ中かを確認するためのフラグ
    public boolean hasSplit() { //スプリットしたかどうか
        return hasSplit;
    }

    public boolean isPlayingSplit() { //2つ目の手札をプレイ中かどうか
        return playingSplit;
    }
    
    //メイン手札が終わったら、スプリット手札に切り替えるためのメソッド
    public void switchToSplitHand() {
        playingSplit = true;
    }

    //プレイヤーの状態をリセットして新しいゲームに備えるための処理
    public void reset() {
        hand.clear(); //プレイヤーの メインの手札（通常の手札）を空にする。
        splitHand.clear(); //スプリット時に使う もう1つの手札（分割したとき用）も空にする。
        hasSplit = false;
        playingSplit = false;
    }
/*
    public int getHandValue() { //手札の合計値を計算するメソッド。
        int total = 0;
        int aceCount = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.toString().contains("A")) aceCount++; //Aが何枚あるかをカウント。
        }
        while (total > 21 && aceCount > 0) { //Aがあり、合計が21を超えていたら「Aの11を1にして調整」するため、total -= 10。
            total -= 10;
            aceCount--;
        }
        return total;
    }

    public void showHand() { //プレイヤーの手札を表示するメソッド。
        for (Card card : hand) {
            System.out.print(card + " ");
        }
        System.out.println("(合計: " + getHandValue() + ")");
    }
    
    public List<Card> getHand() {
        return hand;
    }
*/
}
