package poo.model;

import org.json.JSONArray;
import org.json.JSONObject;

public enum TipoCancha {

    LADRILLO("Cancha de tenis en ladrillo"), CESPED("Cancha de tenis en césped"), OTROS("Otros tipos de superficie");

    private final String value;

    private TipoCancha(String value) {
        this.value = value;
    }

    /**
     * Devuelve el valor de un constante enumerada en formato humano
     * Ejemplo: System.out.println(tp.getValue()); // devuelve: En camino
     * @return El valor del argumento value, recibido por el constructor
     */
    public String getValue() {
        return value;
    }

    /**
     * Dado un string, devuelve la constante enumerada correspondiente. Ejemplo:
     * TipoEstado.getEnum("En camino") devuelve TipoEstado.EN_CAMINO
     * no confundir con TipoEstado.valueOf("CONSTANTE_ENUMERADA")
     * @param value La expresión para humanos correspondiente a la constante enumerada
     * @return La constante enumerada
     */
    public static TipoCancha getEnum(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Se esperaba una categoría de producto");
        }

        value = value.trim();

        for (TipoCancha v : values()) {
            if (value.equalsIgnoreCase(v.getValue())) {
                return v;
            }
        }

        throw new IllegalArgumentException("No se encontró la categoría de producto " + value);
    }

    public static JSONObject getAll() {
        JSONArray jsonArr = new JSONArray();
        for (TipoCancha tp : values()) {
            jsonArr.put(new JSONObject().put("ordinal", tp.ordinal()).put("key", tp).put("value", tp.value));
        }
        return new JSONObject().put("message", "ok").put("data", jsonArr);
    }

    @Override
    public String toString() {
        return value;
    }
}
