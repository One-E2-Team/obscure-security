package rs.ac.uns.ftn.e2.onee2team.security.pki.xssfilter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

public class XSSUtils {

    public static String stripXSS(String value) {
        if (value == null) {
            return null;
        }
        value = ESAPI.encoder()
            .canonicalize(value)
              .replaceAll("\0", "");
        return Jsoup.clean(value, Whitelist.none());
    }

}
