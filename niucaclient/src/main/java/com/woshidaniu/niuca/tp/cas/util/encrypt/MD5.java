package com.woshidaniu.niuca.tp.cas.util.encrypt;


public class MD5 {
    static final int S11 = 7;
    static final int S12 = 12;
    static final int S13 = 17;
    static final int S14 = 22;
    static final int S21 = 5;
    static final int S22 = 9;
    static final int S23 = 14;
    static final int S24 = 20;
    static final int S31 = 4;
    static final int S32 = 11;
    static final int S33 = 16;
    static final int S34 = 23;
    static final int S41 = 6;
    static final int S42 = 10;
    static final int S43 = 15;
    static final int S44 = 21;
    static final byte[] PADDING = new byte[]{-128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private long[] state = new long[4];
    private long[] count = new long[2];
    private byte[] buffer = new byte[64];
    public String digestHexStr;
    private byte[] digest = new byte[16];

    public String getMD5ofStr(String inbuf) {
        this.md5Init();
        this.md5Update(inbuf.getBytes(), inbuf.length());
        this.md5Final();
        this.digestHexStr = "";

        for(int i = 0; i < 16; ++i) {
            this.digestHexStr = this.digestHexStr + byteHEX(this.digest[i]);
        }

        return this.digestHexStr;
    }

    public MD5() {
        this.md5Init();
    }

    private void md5Init() {
        this.count[0] = 0L;
        this.count[1] = 0L;
        this.state[0] = 1732584193L;
        this.state[1] = 4023233417L;
        this.state[2] = 2562383102L;
        this.state[3] = 271733878L;
    }

    private long F(long x, long y, long z) {
        return x & y | ~x & z;
    }

    private long G(long x, long y, long z) {
        return x & z | y & ~z;
    }

    private long H(long x, long y, long z) {
        return x ^ y ^ z;
    }

    private long I(long x, long y, long z) {
        return y ^ (x | ~z);
    }

    private long FF(long a, long b, long c, long d, long x, long s, long ac) {
        a += this.F(b, c, d) + x + ac;
        a = (long)((int)a << (int)s | (int)a >>> (int)(32L - s));
        a += b;
        return a;
    }

    private long GG(long a, long b, long c, long d, long x, long s, long ac) {
        a += this.G(b, c, d) + x + ac;
        a = (long)((int)a << (int)s | (int)a >>> (int)(32L - s));
        a += b;
        return a;
    }

    private long HH(long a, long b, long c, long d, long x, long s, long ac) {
        a += this.H(b, c, d) + x + ac;
        a = (long)((int)a << (int)s | (int)a >>> (int)(32L - s));
        a += b;
        return a;
    }

    private long II(long a, long b, long c, long d, long x, long s, long ac) {
        a += this.I(b, c, d) + x + ac;
        a = (long)((int)a << (int)s | (int)a >>> (int)(32L - s));
        a += b;
        return a;
    }

    private void md5Update(byte[] inbuf, int inputLen) {
        byte[] block = new byte[64];
        int index = (int)(this.count[0] >>> 3) & 63;
        long[] var10000 = this.count;
        if ((var10000[0] += (long)(inputLen << 3)) < (long)(inputLen << 3)) {
            int var10002 = (int) this.count[1]++;
        }

        var10000 = this.count;
        var10000[1] += (long)(inputLen >>> 29);
        int partLen = 64 - index;
        int i;
        if (inputLen >= partLen) {
            this.md5Memcpy(this.buffer, inbuf, index, 0, partLen);
            this.md5Transform(this.buffer);

            for(i = partLen; i + 63 < inputLen; i += 64) {
                this.md5Memcpy(block, inbuf, 0, i, 64);
                this.md5Transform(block);
            }

            index = 0;
        } else {
            i = 0;
        }

        this.md5Memcpy(this.buffer, inbuf, index, i, inputLen - i);
    }

    private void md5Final() {
        byte[] bits = new byte[8];
        this.Encode(bits, this.count, 8);
        int index = (int)(this.count[0] >>> 3) & 63;
        int padLen = index < 56 ? 56 - index : 120 - index;
        this.md5Update(PADDING, padLen);
        this.md5Update(bits, 8);
        this.Encode(this.digest, this.state, 16);
    }

    private void md5Memcpy(byte[] output, byte[] input, int outpos, int inpos, int len) {
        for(int i = 0; i < len; ++i) {
            output[outpos + i] = input[inpos + i];
        }

    }

    private void md5Transform(byte[] block) {
        long a = this.state[0];
        long b = this.state[1];
        long c = this.state[2];
        long d = this.state[3];
        long[] x = new long[16];
        this.Decode(x, block, 64);
        a = this.FF(a, b, c, d, x[0], 7L, 3614090360L);
        d = this.FF(d, a, b, c, x[1], 12L, 3905402710L);
        c = this.FF(c, d, a, b, x[2], 17L, 606105819L);
        b = this.FF(b, c, d, a, x[3], 22L, 3250441966L);
        a = this.FF(a, b, c, d, x[4], 7L, 4118548399L);
        d = this.FF(d, a, b, c, x[5], 12L, 1200080426L);
        c = this.FF(c, d, a, b, x[6], 17L, 2821735955L);
        b = this.FF(b, c, d, a, x[7], 22L, 4249261313L);
        a = this.FF(a, b, c, d, x[8], 7L, 1770035416L);
        d = this.FF(d, a, b, c, x[9], 12L, 2336552879L);
        c = this.FF(c, d, a, b, x[10], 17L, 4294925233L);
        b = this.FF(b, c, d, a, x[11], 22L, 2304563134L);
        a = this.FF(a, b, c, d, x[12], 7L, 1804603682L);
        d = this.FF(d, a, b, c, x[13], 12L, 4254626195L);
        c = this.FF(c, d, a, b, x[14], 17L, 2792965006L);
        b = this.FF(b, c, d, a, x[15], 22L, 1236535329L);
        a = this.GG(a, b, c, d, x[1], 5L, 4129170786L);
        d = this.GG(d, a, b, c, x[6], 9L, 3225465664L);
        c = this.GG(c, d, a, b, x[11], 14L, 643717713L);
        b = this.GG(b, c, d, a, x[0], 20L, 3921069994L);
        a = this.GG(a, b, c, d, x[5], 5L, 3593408605L);
        d = this.GG(d, a, b, c, x[10], 9L, 38016083L);
        c = this.GG(c, d, a, b, x[15], 14L, 3634488961L);
        b = this.GG(b, c, d, a, x[4], 20L, 3889429448L);
        a = this.GG(a, b, c, d, x[9], 5L, 568446438L);
        d = this.GG(d, a, b, c, x[14], 9L, 3275163606L);
        c = this.GG(c, d, a, b, x[3], 14L, 4107603335L);
        b = this.GG(b, c, d, a, x[8], 20L, 1163531501L);
        a = this.GG(a, b, c, d, x[13], 5L, 2850285829L);
        d = this.GG(d, a, b, c, x[2], 9L, 4243563512L);
        c = this.GG(c, d, a, b, x[7], 14L, 1735328473L);
        b = this.GG(b, c, d, a, x[12], 20L, 2368359562L);
        a = this.HH(a, b, c, d, x[5], 4L, 4294588738L);
        d = this.HH(d, a, b, c, x[8], 11L, 2272392833L);
        c = this.HH(c, d, a, b, x[11], 16L, 1839030562L);
        b = this.HH(b, c, d, a, x[14], 23L, 4259657740L);
        a = this.HH(a, b, c, d, x[1], 4L, 2763975236L);
        d = this.HH(d, a, b, c, x[4], 11L, 1272893353L);
        c = this.HH(c, d, a, b, x[7], 16L, 4139469664L);
        b = this.HH(b, c, d, a, x[10], 23L, 3200236656L);
        a = this.HH(a, b, c, d, x[13], 4L, 681279174L);
        d = this.HH(d, a, b, c, x[0], 11L, 3936430074L);
        c = this.HH(c, d, a, b, x[3], 16L, 3572445317L);
        b = this.HH(b, c, d, a, x[6], 23L, 76029189L);
        a = this.HH(a, b, c, d, x[9], 4L, 3654602809L);
        d = this.HH(d, a, b, c, x[12], 11L, 3873151461L);
        c = this.HH(c, d, a, b, x[15], 16L, 530742520L);
        b = this.HH(b, c, d, a, x[2], 23L, 3299628645L);
        a = this.II(a, b, c, d, x[0], 6L, 4096336452L);
        d = this.II(d, a, b, c, x[7], 10L, 1126891415L);
        c = this.II(c, d, a, b, x[14], 15L, 2878612391L);
        b = this.II(b, c, d, a, x[5], 21L, 4237533241L);
        a = this.II(a, b, c, d, x[12], 6L, 1700485571L);
        d = this.II(d, a, b, c, x[3], 10L, 2399980690L);
        c = this.II(c, d, a, b, x[10], 15L, 4293915773L);
        b = this.II(b, c, d, a, x[1], 21L, 2240044497L);
        a = this.II(a, b, c, d, x[8], 6L, 1873313359L);
        d = this.II(d, a, b, c, x[15], 10L, 4264355552L);
        c = this.II(c, d, a, b, x[6], 15L, 2734768916L);
        b = this.II(b, c, d, a, x[13], 21L, 1309151649L);
        a = this.II(a, b, c, d, x[4], 6L, 4149444226L);
        d = this.II(d, a, b, c, x[11], 10L, 3174756917L);
        c = this.II(c, d, a, b, x[2], 15L, 718787259L);
        b = this.II(b, c, d, a, x[9], 21L, 3951481745L);
        long[] var10000 = this.state;
        var10000[0] += a;
        var10000 = this.state;
        var10000[1] += b;
        var10000 = this.state;
        var10000[2] += c;
        var10000 = this.state;
        var10000[3] += d;
    }

    private void Encode(byte[] output, long[] input, int len) {
        int i = 0;

        for(int j = 0; j < len; j += 4) {
            output[j] = (byte)((int)(input[i] & 255L));
            output[j + 1] = (byte)((int)(input[i] >>> 8 & 255L));
            output[j + 2] = (byte)((int)(input[i] >>> 16 & 255L));
            output[j + 3] = (byte)((int)(input[i] >>> 24 & 255L));
            ++i;
        }

    }

    private void Decode(long[] output, byte[] input, int len) {
        int i = 0;

        for(int j = 0; j < len; j += 4) {
            output[i] = b2iu(input[j]) | b2iu(input[j + 1]) << 8 | b2iu(input[j + 2]) << 16 | b2iu(input[j + 3]) << 24;
            ++i;
        }

    }

    public static long b2iu(byte b) {
        return (long)(b < 0 ? b & 255 : b);
    }

    public static String byteHEX(byte ib) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[]{Digit[ib >>> 4 & 15], Digit[ib & 15]};
        String s = new String(ob);
        return s;
    }

    public static void main(String[] args) {
        MD5 md5 = new MD5();
        String str = "js01ZFSSOKEY2012-11-0215:18:48teacher";
        String str1 = md5.getMD5ofStr(str);
        System.out.println(str1);
    }
}
