package com.blackjack.app;

public class Dealer extends Player { //メソッドは Player から自動的に引き継いで使えます（これが継承）。
    public boolean shouldHit() { //ディーラーがもう1枚引くべきかどうか」 を判断するメソッド。
        return getHandValue() < 17;
    }
}
