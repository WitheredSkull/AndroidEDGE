package com.daniel.edge.utils.safety;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class EdgeBase64Utils {
    /**
     * <p>转为unicode 编码<p>
     *
     * @param str
     * @return unicodeString
     */
    public static String encode(String str) {
        String prifix = "\\u";
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            String code = prifix + format(Integer.toHexString(c));
            unicode.append(code);
        }
        return unicode.toString();
    }

    /**
     * <p>unicode 解码<p>
     *
     * @param unicode
     * @return originalString
     */
    public static String decode(String unicode) {
        StringBuffer str = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            str.append((char) data);
        }
        return str.length() > 0 ? str.toString() : unicode;
    }

    /**
     * <p>转为数据库查询编码<p>
     * 向数据库查询时,\\u需要转为%
     *
     * @param str
     * @return
     */
    public static String encodeDBSearchParam(String str) {
        str = encode(str);
        str = str.replace("\\", "%");
        return str;
    }

    /**
     * <p>解码数据库查询参数<p>
     * 数据库查询后,回传参数% 转回\\u
     *
     * @param str
     * @return
     */
    public static String decodeDBSearchParam(String str) {
        str = str.replace("%", "\\");
        str = decode(str);
        return str;
    }

    /**
     * 为长度不足4位的unicode 值补零
     *
     * @param str
     * @return
     */
    private static String format(String str) {
        for (int i = 0, l = 4 - str.length(); i < l; i++) {
            str = "0" + str;
        }
        return str;
    }

    public static void main(String arg[]){
        System.out.print(URLDecoder.decode("baiduboxapp://utils?action=sendIntent&minver=7.4&params=%7B%22intent%22%3A%22intent%3A%23Intent%3Baction%3Dcom.baidu.searchbox.action.HOME%3Bpackage%3Dcom.baidu.searchbox%3BS.targetCommand%3D%257B%2522mode%2522%253A%25222%2522%252C%2522url%2522%253A%2522https%253A%252F%252Fm.baidu.com%252Fs%253Fword%253D%2525E4%2525BB%25258A%2525E6%252597%2525A5%2525E6%252596%2525B0%2525E9%2525B2%25259C%2525E4%2525BA%25258B%2522%252C%2522min_v%2522%253A%252225165824%2522%257D%3Bend%22%7D&needlog=1&logargs=%7B%22source%22%3A%221021206f%22%2C%22from%22%3A%22openbox%22%2C%22page%22%3A%22chrome%22%2C%22type%22%3A%22%22%2C%22value%22%3A%22%22%2C%22channel%22%3A%221021206t%22%2C%22extlog%22%3A%7B%22ext_content%22%3A%7B%22from%22%3A%22844b%22%2C%22browserid%22%3A%2224%22%2C%22qid%22%3A%222177378042%22%2C%22timestamp%22%3A1554885382007%7D%7D%2C%22baiduId%22%3A%2274FDCB6D0EDE8A1067D192C5948F4735%3AFG%3D1%22%2C%22app_now%22%3A%22chrome_1554885382018_3372283275%22%2C%22yyb_pkg%22%3A%22com.baidu.searchbox%22%2C%22idmData%22%3A%7B%7D%2C%22matrix%22%3A%22main%22%7D"));
    }
}
