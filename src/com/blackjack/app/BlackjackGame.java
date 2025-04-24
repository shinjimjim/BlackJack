/*package com.blackjack.app;

import java.util.Scanner;

public class BlackjackGame { //CUIでの実行クラスs
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) { // ここで try-with-resources を使用　「ユーザーの入力（キーボード）を受け取るためのクラス。」
            Deck deck = new Deck(); // 52枚のカードを持ったデッキを作る
            Player player = new Player(); // プレイヤー作成
            Dealer dealer = new Dealer(); // ディーラー作成

            // 初期カード配布　プレイヤーとディーラーに 2枚ずつカードを配る。　drawCard() は山札の一番上からカードを引くメソッド。
            player.addCard(deck.drawCard());
            player.addCard(deck.drawCard());
            dealer.addCard(deck.drawCard());
            dealer.addCard(deck.drawCard());

            // 手札表示　プレイヤーの手札（カードと合計点）を表示。
            System.out.println("あなたの手札:");
            player.showHand();
            System.out.println("ディーラーの手札:");
            dealer.showHand(); // 本来なら1枚だけ見せるのが普通

            // プレイヤーのターン
            while (true) { //true なので、明示的に break するまでずっとループが続く。
                System.out.println("ヒット（h） or スタンド（s）?");
                String choice = scanner.next();
                if (choice.equalsIgnoreCase("h")) {
                    player.addCard(deck.drawCard());
                    player.showHand();
                    if (player.getHandValue() > 21) {
                        System.out.println("バスト！（負け）");
                        return; //main メソッドから抜ける（＝ゲーム終了）。
                    }
                } else {
                    break; //while ループを終了 → プレイヤーのターンが終わって、次はディーラーのターンへ。
                }
            }

            // ディーラーのターン
            System.out.println("ディーラーのターン:");
            while (dealer.shouldHit()) { //shouldHit() が true の間、カードを引き続ける。
                dealer.addCard(deck.drawCard());
            }
            dealer.showHand();

            // 勝敗判定
            int playerScore = player.getHandValue();
            int dealerScore = dealer.getHandValue();

            if (dealerScore > 21 || playerScore > dealerScore) {
                System.out.println("あなたの勝ち！");
            } else if (playerScore == dealerScore) {
                System.out.println("引き分け！");
            } else {
                System.out.println("ディーラーの勝ち！");
            }
        } // tryブロックを抜けると scanner が自動で閉じられる
    }
}*/
