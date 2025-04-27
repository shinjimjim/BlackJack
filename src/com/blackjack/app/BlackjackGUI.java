package com.blackjack.app;

import java.awt.BorderLayout; //上下左右に部品を配置
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton; //ヒット、スタンド用ボタン
import javax.swing.JFrame; //全体のウィンドウ枠
import javax.swing.JLabel; //勝敗などのメッセージ表示
import javax.swing.JPanel; //複数の部品をまとめて配置する
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class BlackjackGUI extends JFrame {
	// ゲーム用クラス
    private Deck deck;
    private Player player;
    private Dealer dealer;
    // CUI部品
/*
    private JTextArea playerArea;
    private JTextArea dealerArea;
*/
    private JPanel playerPanel;
    private JPanel dealerPanel;
    private JLabel statusLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton replayButton; // 追加する再プレイボタン
    private JButton splitButton;
    private JButton doubleDownButton; // ダブルダウンボタンを追加
    private JLabel playerValueLabel;
    private JLabel dealerValueLabel; //手札の合計値表示
    
    private boolean isSplit = false;
    private Player splitPlayer;
    private boolean playingSplitHand = false;
    
    private boolean isFirstTurn = true;
    private boolean splitIsFirstTurn = true; // ← スプリット後用のfirstTurnフラグ
    
    // コンストラクタでウィンドウとゲームの準備
    public BlackjackGUI() {
    	//ウィンドウの設定
        setTitle("ブラックジャック");
        setSize(600, 500); // 少し広めに調整
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
/*
        // ディーラーの表示エリア
        dealerArea = new JTextArea(3, 30); //手札を表示
        dealerArea.setEditable(false); //ユーザーが書き込めないようにする
        dealerArea.setBorder(BorderFactory.createTitledBorder("ディーラー")); //囲い枠とタイトルをつける
        add(dealerArea, BorderLayout.NORTH);
        // プレイヤーの表示エリア
        playerArea = new JTextArea(3, 30); //手札を表示
        playerArea.setEditable(false); //ユーザーが書き込めないようにする
        playerArea.setBorder(BorderFactory.createTitledBorder("あなた")); //囲い枠とタイトルをつける
        add(playerArea, BorderLayout.CENTER);
*/
        // プレイヤーパネル
        playerPanel = new JPanel(new BorderLayout()); //BorderLayout というレイアウト方式を使って、カード表示部分と合計値を上下に分けます。
        playerPanel.setBorder(BorderFactory.createTitledBorder("あなた")); //TitledBorder でパネルに「あなた」と名前をつけています
        JPanel playerCardsPanel = new JPanel(); //playerCardsPanel：カード画像（JLabel）を横に並べて表示するエリア（FlowLayout がデフォルト）。
        playerValueLabel = new JLabel();
        //playerValueLabel = new JLabel("合計: "); //playerValueLabel：その下に表示する「手札の合計」を示すテキスト。
        playerValueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerValueLabel.setForeground(Color.BLUE);
        JPanel playerValuePanel = new JPanel();
        playerValuePanel.setBackground(new Color(230, 240, 255));
        playerValuePanel.add(playerValueLabel);
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER); //カードを中央に配置。
        playerPanel.add(playerValuePanel, BorderLayout.SOUTH); //合計ラベルを下側に配置。

        // ディーラーパネル
        dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("ディーラー"));
        JPanel dealerCardsPanel = new JPanel();
        dealerValueLabel = new JLabel();
        dealerValueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        dealerValueLabel.setForeground(Color.RED);
        JPanel dealerValuePanel = new JPanel();
        dealerValuePanel.setBackground(new Color(255, 230, 230));
        dealerValuePanel.add(dealerValueLabel);
        dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);
        dealerPanel.add(dealerValuePanel, BorderLayout.SOUTH);

        //メインフレームに追加
        add(dealerPanel, BorderLayout.NORTH); //ウィンドウ全体（JFrame）の上側（NORTH）にディーラー
        add(playerPanel, BorderLayout.CENTER); //中央（CENTER）にプレイヤーを配置
        
        // 下部のパネル（ボタン＋ステータスラベル）
        //new BorderLayout() → 中の部品を「上下左右中央」に分けて配置できるレイアウト。
        JPanel bottomPanel = new JPanel(new BorderLayout()); //この bottomPanel に、ボタン群とメッセージラベルを詰め込む。
        // ボタンパネル
        JPanel buttonPanel = new JPanel(); //ボタンだけを載せる小さなパネルを作る。
        hitButton = new JButton("ヒット");
        standButton = new JButton("スタンド"); //「ヒット」「スタンド」ボタンを生成。
        splitButton = new JButton("スプリット");
        splitButton.setEnabled(false);
        doubleDownButton = new JButton("ダブルダウン");
        
        // 再プレイボタン
        replayButton = new JButton("再プレイ");
        replayButton.setEnabled(false); // 初めは無効
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton); //それを buttonPanel に追加 → 並べて表示される。
        buttonPanel.add(splitButton);
        buttonPanel.add(doubleDownButton);
        
        // ステータスラベル
        //SwingConstants.CENTER → ラベルの中の文字を中央揃えにする。
        statusLabel = new JLabel("選んでください", SwingConstants.CENTER);
        // ボタンとラベルを1つの下部パネルにまとめる
        bottomPanel.add(buttonPanel, BorderLayout.CENTER); //buttonPanel（ボタン2つ）を bottomPanel の中央に配置。
        bottomPanel.add(statusLabel, BorderLayout.SOUTH); //statusLabel（メッセージ）を bottomPanel の下部（SOUTH）に配置。
        bottomPanel.add(replayButton, BorderLayout.NORTH); // 再プレイボタンを追加
        add(bottomPanel, BorderLayout.SOUTH); //下部パネル全体（ボタン＋ラベル）を、ウィンドウの下に配置。
        
        // ボタンのイベント処理
        //-> hit() や -> stand() は、ラムダ式でそれぞれのメソッドを呼び出している。
        hitButton.addActionListener(e -> hit());
        standButton.addActionListener(e -> stand());
        replayButton.addActionListener(e -> replay()); // 再プレイボタンのイベント
        splitButton.addActionListener(e -> split());
        doubleDownButton.addActionListener(e -> doubleDown());
        
        //画面表示
        setVisible(true);
        
        // 初期カードを配る
        initializeGame();
    }
    
    private void initializeGame() {
        deck = new Deck();
        player = new Player();
        dealer = new Dealer();
        isSplit = false;
        splitPlayer = null;
        playingSplitHand = false;
        isFirstTurn = true;

        // 初期カード配布
        player.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        
        /*updateUI(false); // 新しいカードを表示　ディーラーの2枚目のカードを隠す*/
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        splitButton.setEnabled(false);
        replayButton.setEnabled(false); // 再プレイボタンを無効にする
        doubleDownButton.setEnabled(player.getHand().size() == 2);
        statusLabel.setText("選んでください");
        
        // スプリットボタンの有効/無効判定を追加（再プレイ時にも必要）
        if (player.getHand().size() == 2 && player.getHand().get(0).getRank().equals(player.getHand().get(1).getRank())) {
        	//プレイヤーの手札が2枚（最初の状態）　 1枚目と2枚目のランク（A〜Kや数字）が同じなら、スプリット可能。
            splitButton.setEnabled(true);
        } else {
            splitButton.setEnabled(false);
        }
        
        // ブラックジャック判定（最初の2枚で21点かつ2枚のみ）
        boolean playerBJ = (player.getHandValue() == 21 && player.getHand().size() == 2);
        boolean dealerBJ = (dealer.getHandValue() == 21 && dealer.getHand().size() == 2);
        
        if (playerBJ || dealerBJ) {
            concludeGame(); //勝敗処理メソッド（後述）
        } else {
        	updateUI(false);
        }
    }
/*
    // GUIに手札を表示する
    private void updateUI() {
        playerArea.setText(getHandString(player)); //player の持っているカードを getHandString() で文字列にする
        dealerArea.setText(getHandString(dealer, false)); // 1枚だけ見せる　false を渡して、「2枚目以降は ?? にする」ようにしてる
    }
*/
    private void updateUI(boolean showDealerCards) { //プレイヤー・ディーラーのカードや点数の表示を最新の状態にして、必要に応じて「スプリット」ボタンの有効/無効も更新する。
    	if (playerPanel == null || dealerPanel == null || playerValueLabel == null || dealerValueLabel == null) {
    		//nullチェックをすることで、プログラムがクラッシュするのを防いでいる。
            System.err.println("GUIコンポーネントが初期化されていません！");
            return; // または例外をスローするなどの適切なエラー処理
        }
    	
    	JPanel playerCardsPanel = (JPanel) playerPanel.getComponent(0); //playerPanel や dealerPanel の中にある カードを並べる専用パネルを取り出す。
        JPanel dealerCardsPanel = (JPanel) dealerPanel.getComponent(0); //getComponent(0) というのは、「そのパネルの中にある最初の子コンポーネントを取り出す」という意味。
        
        if (playerCardsPanel == null || dealerCardsPanel == null) {
            System.err.println("カード表示パネルが初期化されていません！");
            return;
        }
        // プレイヤーとディーラーのカード画像を更新
        playerCardsPanel.removeAll();
        dealerCardsPanel.removeAll(); //新しいカードを表示するために、パネルの中身を一旦クリア
        
        //playingSplitHand（スプリット中かどうか）によって、プレイヤーの通常の手札 or スプリット用手札を選んで表示。
        List<Card> handToShow = playingSplitHand ? splitPlayer.getHand() : player.getHand();
        for (Card card : handToShow) {
            playerCardsPanel.add(createCardImageLabel(card)); //createCardImageLabel(card) でカード画像を JLabel にして、画面に追加している。
        }

        //ディーラーのカードを表示（1枚だけ表にする or 全部表にする）
        for (int i = 0; i < dealer.getHand().size(); i++) { //ディーラーの手札（dealer.getHand()）は List<Card> 型。
            //ここで分岐しているのは、「カードを表向きで表示するか？」という判断。
        	if (i == 0 || showDealerCards) { //i == 0 → 最初の1枚目のカードは 常に表にする
        									 //showDealerCards == true → ディーラーのカードを すべて表にする（勝敗が決まったあとなど）
                dealerCardsPanel.add(createCardImageLabel(dealer.getHand().get(i))); //表向き（open card）
            } else {
                dealerCardsPanel.add(createBackImageLabel()); //裏向き（伏せたカード）
            }
        }
        
        // 合計値ラベルを更新
        int value = playingSplitHand ? splitPlayer.getHandValue() : player.getHandValue();
        //playingSplitHand（スプリット中かどうか）によって、プレイヤーの通常の手札の合計値 or スプリット用手札の合計値を選んで表示。
        playerValueLabel.setText("合計: " + value);
        dealerValueLabel.setText("合計: " + (showDealerCards ? dealer.getHandValue() : "？"));
        //ディーラーの表示は、showDealerCards が false なら "？" にして隠す。

        //パネルを再描画（表示を反映）
        playerPanel.revalidate(); //revalidate() → パネルのレイアウト（配置）を再計算する。
        playerPanel.repaint(); //repaint() → 実際に画面を再描画する（画像などを見た目に反映）。
        dealerPanel.revalidate();
        dealerPanel.repaint();

        //スプリットボタンの有効化/無効化
        if (!isSplit && player.getHand().size() == 2 && //まだスプリットが行われていない。//プレイヤーの手札が 2 枚である。
                player.getHand().get(0).getRank().equals(player.getHand().get(1).getRank())) { //プレイヤーの最初の 2 枚のカードのランクが同じである。
                splitButton.setEnabled(true);
            } else {
                splitButton.setEnabled(false);
            }
        //ダブルダウンボタンの有効化/無効化
        if (isFirstTurn) { //最初のターンかどうかを確認
            boolean canDoubleDown = false;
            if (playingSplitHand) { //スプリット中（playingSplitHandがtrue）なら、splitPlayerの手札を見ます。
            	canDoubleDown = splitPlayer.getHand().size() == 2 && splitIsFirstTurn;
            } else { //普通のプレイ中なら、playerの手札を見ます。
            	canDoubleDown = player.getHand().size() == 2 && isFirstTurn;
            }
            doubleDownButton.setEnabled(canDoubleDown);
        } else {
            doubleDownButton.setEnabled(false); //最初のターンじゃないなら、絶対にダブルダウンできないので、ボタンは常に無効にします。
        }
    }
/*
    // 手札をテキストにする
    private String getHandString(Player p) {
        return getHandString(p, true);
    }
    // プレイヤーまたはディーラーの手札（カード）を、画面に表示するための文字列に変換する処理
    private String getHandString(Player p, boolean showAll) {
    	// showAll = true：全部のカード＆合計値を表示（プレイヤー or ゲーム終了後のディーラー）
    	// showAll = false：2枚目以降を隠す（ディーラー用）
        StringBuilder sb = new StringBuilder(); //StringBuilder は文字列を連結して効率的に作るためのクラス（+= より速い）
        for (int i = 0; i < p.getHand().size(); i++) {
            if (!showAll && i == 1) { //2枚目（i == 1）かつ showAll が false のとき
                sb.append("[??] "); //ディーラーの2枚目のカードを隠す」
            } else {
                sb.append(p.getHand().get(i).toString()).append(" "); //それ以外のカードは .toString() でカード内容を表示
            }
        }
        if (showAll) { //showAll == true のときだけ、カードの合計点数を表示
            sb.append("（合計: ").append(p.getHandValue()).append("）");
        }
        return sb.toString();
    }
*/
    private JLabel createCardImageLabel(Card card) { //Card オブジェクトを受け取って、それに対応するカード画像を表示する JLabel を返すメソッド
        try { //try と catch は、「例外処理（エラー対策）」のための仕組みです。
            // カード画像ファイル名を作成
            String cardName = card.getRank() + "_of_" + card.getSuit() + ".png";
   
            // カード画像を読み込む
            //getClass().getResource(...) は、リソースフォルダの画像ファイルを探してくれるメソッドです。
            //ImageIcon は、Swing で画像を表示するためのクラスです。
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + cardName));
            
            // 画像をリサイズ（例: 幅 70px, 高さ 100px）
            //SCALE_SMOOTH を使うと、綺麗に縮小される。
            Image scaledImage = originalIcon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            return new JLabel(scaledIcon); //JLabel に画像を埋め込んで返します。        

        } catch (Exception e) { //こういう構文
            e.printStackTrace(); //// エラー内容をコンソールに表示するという意味
            return new JLabel("カード画像がありません");
        }
    }
/*
    private String getHandString(Player p) {
        StringBuilder sb = new StringBuilder();
        for (Card card : p.getHand()) {
            sb.append(card.toString()).append(" ");
        }
        sb.append("（合計: ").append(p.getHandValue()).append("）");
        return sb.toString();
    }
*/
    //トランプの裏面画像を表示するJLabel を作るためのもの
    private JLabel createBackImageLabel() {
        try {
            ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));
            //getClass().getResource(...) は、クラスパス内のリソースを取得する方法
            Image scaledImage = backIcon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            //`Image.SCALE_SMOOTH` は、なめらかに縮小するオプション（画質がキレイになる）
            return new JLabel(new ImageIcon(scaledImage));
            //スケーリングした画像を新しい ImageIcon にして、それを JLabel に入れて返している
        } catch (Exception e) {
            e.printStackTrace();
            return new JLabel("裏面画像なし");
        }
    }

    // ボタン処理：ヒット（カードを引く）
    private void hit() {
    	if (playingSplitHand) { //playingSplitHand == true の時、つまりスプリット後の手札をプレイ中。
            splitPlayer.addCard(deck.drawCard()); // スプリット手札にカードを追加
            if (splitPlayer.getHandValue() > 21) {
                statusLabel.setText("スプリット手札：バスト！");
                playingSplitHand = false; // もうスプリット手札のターンは終了
                updateUI(false);
                /*stand(); // 自動的に「スタンド」して次に進む*/
                endGame();
            } else {
                updateUI(false); // まだ続けられるならUIだけ更新
            }
        } else { //通常の手札をプレイ中（スプリットしてない or 最初の手札）
            player.addCard(deck.drawCard()); // 通常の手札にカードを追加
            updateUI(false); // UI更新
            if (player.getHandValue() > 21) {
                if (isSplit) { //スプリット中（isSplit == true）で最初の手札がバストしてたら、自動的にスプリット手札へ移行。
                    statusLabel.setText("最初の手札：バスト！/スプリット手札：選んでください");
                    playingSplitHand = true; // 次はスプリット手札をプレイ
                    updateUI(false);
                } else {
                    statusLabel.setText("バスト！あなたの負け！");
                    endGame(); // ゲーム終了
                }
            }
        }
    }
    
    // ボタン処理：スタンド（勝敗判定）
    private void stand() {
    	if (isSplit && !playingSplitHand) { //isSplit はプレイヤーがスプリットしているかどうか。
    										//playingSplitHand は現在、スプリット後の2枚目の手札をプレイ中か。
            playingSplitHand = true; //スプリットした 2枚目の手札に切り替える。
            isFirstTurn = true;
            statusLabel.setText("スプリット手札に移行！");
            updateUI(false);
            return;
        }
    	//ディーラーの処理
        while (dealer.getHandValue() < 17) {
            dealer.addCard(deck.drawCard());
        }
/*
        dealerArea.setText(getHandString(dealer, true)); //第二引数が true のときは全カード＋点数を表示する仕様
*/
        updateUI(true); // ディーラーのカード全公開
        
        //勝敗判定の準備
        String result = "";
        
        //ディーラーの点数と、プレイヤー（通常手札）の点数を取得。
        int dVal = dealer.getHandValue();
        int p1 = player.getHandValue();
        int p2 = isSplit ? splitPlayer.getHandValue() : -1; //スプリットしていれば、スプリット側の手札の点数も取得。

        //勝敗の判定
        if (!isSplit) {
            result = judgeResult(p1, dVal);
        } else {
            result += "最初の手札: " + judgeResult(p1, dVal) + " / ";
            result += "スプリット手札: " + judgeResult(p2, dVal); //judgeResult() は、点数を比較して「勝ち」「負け」「引き分け」を文字列で返す関数。
        }
        
        //ゲーム終了処理
        statusLabel.setText(result); //勝敗結果を画面に表示。
        endGame(); //ボタンの無効化など終了処理を行います。
    }
    
    //勝敗判定を行うメソッド
    private String judgeResult(int playerVal, int dealerVal) {
        if (playerVal > 21) return "バスト！あなたの負け！";
        if (dealerVal > 21 || playerVal > dealerVal) return "あなたの勝ち！";
        if (playerVal == dealerVal) return "引き分け！";
        return "あなたの負け！";
    }
    
    private void concludeGame() {
    	System.out.println("concludeGame() が呼び出されました");

        boolean playerBJ = (player.getHandValue() == 21 && player.getHand().size() == 2);
        boolean dealerBJ = (dealer.getHandValue() == 21 && dealer.getHand().size() == 2);

        if (playerBJ && dealerBJ) {
        	System.out.println("両者ブラックジャックの処理を実行します。");
            statusLabel.setText("引き分け（両者ブラックジャック）！");
            statusLabel.repaint();
        } else if (playerBJ) {
        	System.out.println("プレイヤーブラックジャックの処理を実行します。");
            statusLabel.setText("あなたのブラックジャック！勝利！");
            statusLabel.repaint();
        } else if (dealerBJ) {
        	System.out.println("ディーラーブラックジャックの処理を実行します。");
            statusLabel.setText("ディーラーのブラックジャック。あなたの負け！");
            statusLabel.repaint();
        } 
        updateUI(true); // ディーラーの手札を公開
        endGame();
    }
    
    // 勝敗決定後の処理
    private void endGame() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false); //ボタンを無効（＝押せなく）する命令。
        splitButton.setEnabled(false);
        replayButton.setEnabled(true); // ゲーム終了後に再プレイボタンを有効にする
        doubleDownButton.setEnabled(false);
    }
    
    //ゲームを再スタートするためのメソッド。
    private void replay() {
        initializeGame(); // ゲームをリセット
    }
    
    //スプリット処理を行うメソッド
    private void split() {
        if (!isSplit && player.getHand().size() == 2 && //!isSplit：まだスプリットしていない（＝一度だけしかスプリットできない）。
            player.getHand().get(0).getRank().equals(player.getHand().get(1).getRank())) { //スプリットできる条件をチェックしている部分。

            isSplit = true; //スプリットしたことを記録しておくフラグ。これで同じゲーム中にもう一度スプリットできないようにする。
            splitPlayer = new Player(); //スプリット後のもう一方の手札を持つ「もう一人の自分」を作成。

            // 1枚ずつに分けて、それぞれにカードを追加
            Card secondCard = player.getHand().remove(1); //2枚目のカードを元の手札から取り除いて、一時的に保存してる。
            splitPlayer.addCard(secondCard); //取り除いた2枚目のカードを splitPlayer に渡す。
            player.addCard(deck.drawCard());
            splitPlayer.addCard(deck.drawCard()); //それぞれの手札に、新しい（2枚目）カードを山札から1枚ずつ配る。

            updateUI(false); //画面表示を更新。カード画像などを再描画して最新状態にする。
            statusLabel.setText("スプリットしました。先に元の手札をプレイ！");
        }
    }
    
    private void doubleDown() {
    	if (playingSplitHand) { // スプリット中なら
            splitPlayer.addCard(deck.drawCard()); // スプリット手札にカードを1枚引く
            updateUI(false);
            
            // スプリットの手札が終わったのでディーラーターンへ
            stand();
        } else { // 通常プレイ中
            player.addCard(deck.drawCard()); // プレイヤーにカードを1枚引く
            updateUI(false);

            if (isSplit) {
                // 1枚目の手札のダブルダウンが終わった → 次に2枚目に切り替える
                playingSplitHand = true;
                isFirstTurn = true; // 2枚目も最初のターン扱い
                updateUI(false);
                statusLabel.setText("スプリット2枚目：選んでください");
            } else {
                // スプリットしてないなら普通にターン終了
                stand();
            }
        }
        isFirstTurn = false; // どちらもダブルダウン後は通常ターン扱い
    }

    // main メソッド（アプリを起動）
    public static void main(String[] args) {
        /*SwingUtilities.invokeLater(() -> new BlackjackGUI());*/ 
    	//SwingUtilities.invokeLater() を使うことで UI処理を安全に別スレッドで実行してる
    	SwingUtilities.invokeLater(BlackjackGUI::new);
    	//BlackjackGUI::newは() -> new BlackjackGUI()と同じ意味
    }
}
