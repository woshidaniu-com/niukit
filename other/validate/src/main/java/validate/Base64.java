//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package validate;

public final class Base64 {
    private static final int _$13 = 19;
    private static final int _$12 = 255;
    private static final int _$11 = 64;
    private static final int _$10 = 24;
    private static final int _$9 = 8;
    private static final int _$8 = 16;
    private static final int _$7 = 6;
    private static final int _$6 = 4;
    private static final int _$5 = -128;
    private static final char _$4 = '=';
    private static final boolean _$3 = false;
    private static final byte[] _$2 = new byte[255];
    private static final char[] _$1 = new char[64];

    public Base64() {
    }

    protected static boolean _$4(char var0) {
        return var0 == ' ' || var0 == '\r' || var0 == '\n' || var0 == '\t';
    }

    protected static boolean _$3(char var0) {
        return var0 == '=';
    }

    protected static boolean _$2(char var0) {
        return _$2[var0] != -1;
    }

    protected static boolean _$1(char var0) {
        return _$4(var0) || _$3(var0) || _$2(var0);
    }

    public static String encode(byte[] var0) {
        return encode(var0, 0);
    }

    public static String encode(byte[] var0, int var1) {
        boolean var2 = true;
        int var27;
        if (var1 == 0) {
            var27 = 19;
        } else if (var1 % 4 == 0) {
            var27 = var1 / 4;
        } else {
            var27 = 19;
        }

        if (var0 == null) {
            return null;
        } else {
            int var3 = var0.length * 8;
            if (var3 == 0) {
                return "";
            } else {
                int var4 = var3 % 24;
                int var5 = var3 / 24;
                int var6 = var4 == 0 ? var5 : var5 + 1;
                int var7 = (var6 - 1) / var27 + 1;
                Object var8 = null;
                char[] var28;
                if (var1 == 0) {
                    var28 = new char[var6 * 4];
                } else {
                    var28 = new char[var6 * 4 + var7];
                }

                boolean var9 = false;
                boolean var10 = false;
                boolean var11 = false;
                boolean var12 = false;
                boolean var13 = false;
                int var14 = 0;
                int var15 = 0;
                int var16 = 0;

                byte var19;
                byte var20;
                byte var21;
                byte var22;
                byte var23;
                byte var24;
                for(int var17 = 0; var17 < var7 - 1; ++var17) {
                    for(int var18 = 0; var18 < var27; ++var18) {
                        var19 = var0[var15++];
                        var20 = var0[var15++];
                        var21 = var0[var15++];
                        var22 = (byte)(var20 & 15);
                        var23 = (byte)(var19 & 3);
                        var24 = (var19 & -128) != 0 ? (byte)(var19 >> 2 ^ 192) : (byte)(var19 >> 2);
                        byte var25 = (var20 & -128) != 0 ? (byte)(var20 >> 4 ^ 240) : (byte)(var20 >> 4);
                        byte var26 = (var21 & -128) != 0 ? (byte)(var21 >> 6 ^ 252) : (byte)(var21 >> 6);
                        var28[var14++] = _$1[var24];
                        var28[var14++] = _$1[var25 | var23 << 4];
                        var28[var14++] = _$1[var22 << 2 | var26];
                        var28[var14++] = _$1[var21 & 63];
                        ++var16;
                    }

                    if (var1 != 0) {
                        var28[var14++] = '\n';
                    }
                }

                byte var29;
                byte var30;
                while(var16 < var5) {
                    var29 = var0[var15++];
                    var30 = var0[var15++];
                    var19 = var0[var15++];
                    var20 = (byte)(var30 & 15);
                    var21 = (byte)(var29 & 3);
                    var22 = (var29 & -128) != 0 ? (byte)(var29 >> 2 ^ 192) : (byte)(var29 >> 2);
                    var23 = (var30 & -128) != 0 ? (byte)(var30 >> 4 ^ 240) : (byte)(var30 >> 4);
                    var24 = (var19 & -128) != 0 ? (byte)(var19 >> 6 ^ 252) : (byte)(var19 >> 6);
                    var28[var14++] = _$1[var22];
                    var28[var14++] = _$1[var23 | var21 << 4];
                    var28[var14++] = _$1[var20 << 2 | var24];
                    var28[var14++] = _$1[var19 & 63];
                    ++var16;
                }

                if (var4 == 8) {
                    var29 = var0[var15];
                    var30 = (byte)(var29 & 3);
                    var19 = (var29 & -128) != 0 ? (byte)(var29 >> 2 ^ 192) : (byte)(var29 >> 2);
                    var28[var14++] = _$1[var19];
                    var28[var14++] = _$1[var30 << 4];
                    var28[var14++] = '=';
                    var28[var14++] = '=';
                } else if (var4 == 16) {
                    var29 = var0[var15];
                    var30 = var0[var15 + 1];
                    var19 = (byte)(var30 & 15);
                    var20 = (byte)(var29 & 3);
                    var21 = (var29 & -128) != 0 ? (byte)(var29 >> 2 ^ 192) : (byte)(var29 >> 2);
                    var22 = (var30 & -128) != 0 ? (byte)(var30 >> 4 ^ 240) : (byte)(var30 >> 4);
                    var28[var14++] = _$1[var21];
                    var28[var14++] = _$1[var22 | var20 << 4];
                    var28[var14++] = _$1[var19 << 2];
                    var28[var14++] = '=';
                }

                if (var1 != 0) {
                    var28[var14] = '\n';
                }

                return new String(var28);
            }
        }
    }

    public static byte[] decode(String var0) {
        if (var0 == null) {
            return null;
        } else {
            char[] var1 = var0.toCharArray();
            int var2 = _$1(var1);
            if (var2 % 4 != 0) {
                return null;
            } else {
                int var3 = var2 / 4;
                if (var3 == 0) {
                    return new byte[0];
                } else {
                    Object var4 = null;
                    boolean var5 = false;
                    boolean var6 = false;
                    boolean var7 = false;
                    boolean var8 = false;
                    boolean var9 = false;
                    boolean var10 = false;
                    boolean var11 = false;
                    boolean var12 = false;
                    boolean var13 = false;
                    boolean var14 = false;
                    int var15 = 0;
                    int var16 = 0;
                    int var17 = 0;
                    byte[] var20 = new byte[var3 * 3];

                    while(true) {
                        char var10000;
                        int var10001;
                        char var10002;
                        byte var18;
                        byte var19;
                        byte var21;
                        byte var22;
                        char var23;
                        char var24;
                        char var25;
                        char var26;
                        if (var15 >= var3 - 1) {
                            var10001 = var17++;
                            var10000 = var1[var10001];
                            var10002 = var1[var10001];
                            var23 = var10000;
                            if (_$2(var10002)) {
                                var10001 = var17++;
                                var10000 = var1[var10001];
                                var10002 = var1[var10001];
                                var24 = var10000;
                                if (_$2(var10002)) {
                                    var21 = _$2[var23];
                                    var22 = _$2[var24];
                                    var25 = var1[var17++];
                                    var26 = var1[var17++];
                                    if (_$2(var25) && _$2(var26)) {
                                        var18 = _$2[var25];
                                        var19 = _$2[var26];
                                        var20[var16++] = (byte)(var21 << 2 | var22 >> 4);
                                        var20[var16++] = (byte)((var22 & 15) << 4 | var18 >> 2 & 15);
                                        var20[var16++] = (byte)(var18 << 6 | var19);
                                        return var20;
                                    }

                                    if (_$3(var25) && _$3(var26)) {
                                        if ((var22 & 15) != 0) {
                                            return null;
                                        }

                                        byte[] var27 = new byte[var15 * 3 + 1];
                                        System.arraycopy(var20, 0, var27, 0, var15 * 3);
                                        var27[var16] = (byte)(var21 << 2 | var22 >> 4);
                                        return var27;
                                    }

                                    if (!_$3(var25) && _$3(var26)) {
                                        var18 = _$2[var25];
                                        if ((var18 & 3) != 0) {
                                            return null;
                                        }

                                        byte[] var28 = new byte[var15 * 3 + 2];
                                        System.arraycopy(var20, 0, var28, 0, var15 * 3);
                                        var28[var16++] = (byte)(var21 << 2 | var22 >> 4);
                                        var28[var16] = (byte)((var22 & 15) << 4 | var18 >> 2 & 15);
                                        return var28;
                                    }

                                    return null;
                                }
                            }

                            return null;
                        }

                        var10001 = var17++;
                        var10000 = var1[var10001];
                        var10002 = var1[var10001];
                        var23 = var10000;
                        if (!_$2(var10002)) {
                            break;
                        }

                        var10001 = var17++;
                        var10000 = var1[var10001];
                        var10002 = var1[var10001];
                        var24 = var10000;
                        if (!_$2(var10002)) {
                            break;
                        }

                        var10001 = var17++;
                        var10000 = var1[var10001];
                        var10002 = var1[var10001];
                        var25 = var10000;
                        if (!_$2(var10002)) {
                            break;
                        }

                        var10001 = var17++;
                        var10000 = var1[var10001];
                        var10002 = var1[var10001];
                        var26 = var10000;
                        if (!_$2(var10002)) {
                            break;
                        }

                        var21 = _$2[var23];
                        var22 = _$2[var24];
                        var18 = _$2[var25];
                        var19 = _$2[var26];
                        var20[var16++] = (byte)(var21 << 2 | var22 >> 4);
                        var20[var16++] = (byte)((var22 & 15) << 4 | var18 >> 2 & 15);
                        var20[var16++] = (byte)(var18 << 6 | var19);
                        ++var15;
                    }

                    return null;
                }
            }
        }
    }

    protected static int _$1(char[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 0;
            int var2 = var0.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                if (!_$4(var0[var3])) {
                    var0[var1++] = var0[var3];
                }
            }

            return var1;
        }
    }

    static {
        int var0;
        for(var0 = 0; var0 < 255; ++var0) {
            _$2[var0] = -1;
        }

        for(var0 = 90; var0 >= 65; --var0) {
            _$2[var0] = (byte)(var0 - 65);
        }

        for(var0 = 122; var0 >= 97; --var0) {
            _$2[var0] = (byte)(var0 - 97 + 26);
        }

        for(var0 = 57; var0 >= 48; --var0) {
            _$2[var0] = (byte)(var0 - 48 + 52);
        }

        _$2[43] = 62;
        _$2[47] = 63;

        for(var0 = 0; var0 <= 25; ++var0) {
            _$1[var0] = (char)(65 + var0);
        }

        var0 = 26;

        int var1;
        for(var1 = 0; var0 <= 51; ++var1) {
            _$1[var0] = (char)(97 + var1);
            ++var0;
        }

        var0 = 52;

        for(var1 = 0; var0 <= 61; ++var1) {
            _$1[var0] = (char)(48 + var1);
            ++var0;
        }

        _$1[62] = '+';
        _$1[63] = '/';
    }
}
