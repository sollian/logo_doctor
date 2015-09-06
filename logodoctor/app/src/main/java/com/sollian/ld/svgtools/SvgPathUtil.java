package com.sollian.ld.svgtools;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sollian on 2015/9/6.
 */
public class SvgPathUtil {
    public static String[] getSvgPath(Context context) {
        String result = null;
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open("logo_doctor.svg");
            Document doc = Jsoup.parse(is, "UTF-8", "");
            result = doc.select("path").get(0).attr("d");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result == null ? null : new String[]{result};
    }
}
