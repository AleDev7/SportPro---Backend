package poo.model;

import java.io.IOException;

import org.json.JSONObject;

import poo.helpers.Utils;

public abstract class InstalacionDeportiva {

    protected String id;
    protected double ancho;
    protected double largo;
    protected double valorHora;
    protected String descripcion;
    protected double area;

    public InstalacionDeportiva() {
        this.id = Utils.randomId(10);
        this.ancho = 0;
        this.largo = 0;
        this.area = 0;
        this.valorHora = 0;
        this.descripcion = "Instalacion deportiva sin descripcion";
    }

    public InstalacionDeportiva(String id) {
        this.id = id;
    }

    public InstalacionDeportiva(String id, double ancho, double largo, double valorHora, String descripcion) {
        this.id = id;
        this.ancho = ancho;
        this.largo = largo;
        this.area = getArea();
        this.valorHora = valorHora;
        this.descripcion = descripcion;
    }

    public InstalacionDeportiva(JSONObject json) {
        this.id = json.optString("id", Utils.randomId(10));
        this.ancho = json.optDouble("ancho", 0);
        this.largo = json.optDouble("largo", 0);
        this.area = getArea();
        this.valorHora = json.optDouble("valorHora", 0);
        this.descripcion = json.optString("descripcion", "Instalacion deportiva sin descripcion");
    }

    public InstalacionDeportiva(InstalacionDeportiva instalacion) {
        this.id = instalacion.id;
        this.ancho = instalacion.ancho;
        this.largo = instalacion.largo;
        this.valorHora = instalacion.valorHora;
        this.descripcion = instalacion.descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getAncho() {
        return ancho;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getLargo() {
        return largo;
    }

    public double getArea() {
        area = largo * ancho;
        return area;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
    }

    public double getValorHora() {
        return valorHora;
    }

    public abstract Double getCostoFuncionamiento(JSONObject funcionamiento);

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static InstalacionDeportiva getInstance(JSONObject json) throws IOException {
        if (json.has("olimpica")) {
            return new Piscina(json);
        } else if (json.has("tipoCancha")) {
            return new CanchaTenis(json);
        } else if (json.has("graderia")) {
            return new CanchaMultiproposito(json);
        } else {
            throw new IllegalArgumentException("Se esperaba una instalacion deportiva ||");
        }
    }

    public abstract String getTipoInstalacion();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InstalacionDeportiva that = (InstalacionDeportiva) obj;
        return this.id.equals(that.id);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("ancho", ancho);
        json.put("largo", largo);
        json.put("area", getArea());
        json.put("tipoInstalacion", getTipoInstalacion());
        json.put("valorHora", valorHora);
        json.put("descripcion", descripcion);
        return json;
    }

    @Override
    public String toString() {
        return "InstalacionDeportiva{" +
                "id='" + id + '\'' +
                ", ancho=" + ancho +
                ", largo=" + largo +
                ", area=" + getArea() +
                ", tipoInstalacion=" + getTipoInstalacion() +
                ", valorHora=" + valorHora +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
