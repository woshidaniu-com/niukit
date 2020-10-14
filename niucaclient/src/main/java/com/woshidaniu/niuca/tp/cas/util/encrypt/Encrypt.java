package com.woshidaniu.niuca.tp.cas.util.encrypt;


public class Encrypt {
    private String key = "Encrypt01";

    public Encrypt() {
    }

    public String encrypt(String PlainStr) {
        String NewStr = "";
        int Pos = 0;

        for(int i = 0; i < PlainStr.length(); ++i) {
            String PStr = PlainStr.substring(i, i + 1);
            String KeyStr = this.key.substring(Pos, Pos + 1);
            char PChar = PStr.charAt(0);
            char KeyChar = KeyStr.charAt(0);
            if ((PChar ^ KeyChar) >= 32 && (PChar ^ KeyChar) <= 126 && PChar >= 0 && PChar <= 255) {
                PChar ^= KeyChar;
                NewStr = NewStr + String.valueOf(PChar);
            } else {
                NewStr = NewStr + PStr;
            }

            ++Pos;
            if (Pos == this.key.length()) {
                Pos = 0;
            }
        }

        if (NewStr.length() % 2 == 0) {
            String Side1 = NewStr.substring(0, NewStr.length() / 2);
            String Side2 = NewStr.substring(NewStr.length() / 2, NewStr.length());
            StringBuffer s1 = new StringBuffer(Side1);
            s1.reverse();
            Side1 = String.valueOf(s1);
            StringBuffer s2 = new StringBuffer(Side2);
            s2.reverse();
            Side2 = String.valueOf(s2);
            NewStr = Side1 + Side2;
        }

        return NewStr;
    }

    public String decrypt(String PlainStr) {
        String NewStr = "";
        int Pos = 0;
        if (PlainStr.length() % 2 == 0) {
            String Side1 = PlainStr.substring(0, PlainStr.length() / 2);
            String Side2 = PlainStr.substring(PlainStr.length() / 2, PlainStr.length());
            StringBuffer s1 = new StringBuffer(Side1);
            s1.reverse();
            Side1 = String.valueOf(s1);
            StringBuffer s2 = new StringBuffer(Side2);
            s2.reverse();
            Side2 = String.valueOf(s2);
            PlainStr = Side1 + Side2;
        }

        for(int i = 0; i < PlainStr.length(); ++i) {
            String PStr = PlainStr.substring(i, i + 1);
            String KeyStr = this.key.substring(Pos, Pos + 1);
            char PChar = PStr.charAt(0);
            char KeyChar = KeyStr.charAt(0);
            if ((PChar ^ KeyChar) >= 32 && (PChar ^ KeyChar) <= 126 && PChar >= 0 && PChar <= 255) {
                PChar ^= KeyChar;
                NewStr = NewStr + String.valueOf(PChar);
            } else {
                NewStr = NewStr + PStr;
            }

            ++Pos;
            if (Pos == this.key.length()) {
                Pos = 0;
            }
        }

        return NewStr;
    }

    public static void main(String[] args) {
        Encrypt e = new Encrypt();
        System.out.println(e.decrypt("fosf?0[J+t"));
        System.out.println(e.encrypt("zfbaseframe"));
        System.out.println(e.encrypt("niuca"));
    }
}
