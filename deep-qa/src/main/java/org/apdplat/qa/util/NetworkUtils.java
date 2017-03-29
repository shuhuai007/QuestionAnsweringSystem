package org.apdplat.qa.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by jiezhou on 29/03/2017.
 */
public class NetworkUtils {

    private static final String DEFAULT_URL_TO_BE_TESTED = "http://baidu.com/";

    /**
     * Check if the connection is reachable.
     *
     * @return true if connected
     */
    public static boolean isConnected(String urlStr) {
        Boolean connected = true;
        try {
            URL url = new URL(urlStr);
            InputStream in = url.openStream();
            in.close();
        } catch (IOException e) {
            return false;
        }
        return connected;
    }

    public static boolean isConnected() {
        return isConnected(DEFAULT_URL_TO_BE_TESTED);
    }

    public static void main(String[] args) {
        System.out.println(isConnected());
    }
}
