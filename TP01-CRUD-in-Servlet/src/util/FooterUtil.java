package util;

import java.io.PrintWriter;

public class FooterUtil {

    public static void renderFooter(PrintWriter out) {
        out.println("<div style='position:fixed; bottom:0; left:0; width:100%; " +
                "background:#f2f2f2; color:#333; text-align:center; padding:10px; " +
                "font-family:sans-serif; font-size:14px;'>");
        out.println("Desenvolvido por: Ricardo Queiroz e Giovanne Aquino");
        out.println("</div>");
    }

}
