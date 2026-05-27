package wpos.util;

import java.text.NumberFormat;
import java.util.Locale;

/** Utilitários de formatação monetária para o Brasil. */
public final class Formatador {

    private static final NumberFormat BRL =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private Formatador() {}

    public static String moeda(java.math.BigDecimal valor) {
        return BRL.format(valor);
    }
}
