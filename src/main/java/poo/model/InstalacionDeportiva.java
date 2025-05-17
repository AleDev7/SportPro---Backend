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
    
    @SuppressWarnings("unused")
    public InstalacionDeportiva() {
        // sólo un ID de 5 caracteres aleatorios y la descripción "Instalación deportiva sin descripción"
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
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            this.id = Utils.getRandomKey(10); // o la longitud que desees
        }
        this.ancho = json.optDouble("ancho", 0);
        this.largo = json.optDouble("largo", 0);
        if (json.has("area")) {
            this.area = json.getDouble("area");
        } else {
            this.area = getArea(); // o la longitud que desees
        }
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
        if (ancho < 1.2) {
        // no aceptar anchos inferiores a 1.2 metros
        // si el ancho es mayor que el largo y largo es mayor a cero, generar un IllegalArgumentException
            throw new IllegalArgumentException("El ancho debe ser superior a 1.2 metros");
        } else {
            this.ancho = ancho;
        }
    }

    public double getAncho() {
        return ancho;
     }


    public void setLargo(double largo) {
        if (largo < 1.2) {
        // no aceptar largos inferiores a 1.2 metros
        // si el largo es menor que el ancho y el ancho es mayor a cero, generar un IllegalArgumentException
            throw new IllegalArgumentException("El largo debe ser superior a 1.2 metros");
        } else {
            this.largo = largo;
        }
        
    }

    public double getLargo() { 
        return largo;
    }

    public double getArea() {
        area = largo * ancho;
        return area;
    }


    public void setValorHora(double valorHora) {
        if (valorHora < 1000) { 
        // El valor de la hora no puede ser menor que 1000
        throw new IllegalArgumentException("El valor de la hora no puede ser inferior a 1000");
        } else {
            this.valorHora = valorHora;
        }
    }

    public double getValorHora() { 
        return valorHora;
    }

    public abstract Double getCostoFuncionamiento(JSONObject funcionamiento);
        
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.length() > 200 || descripcion.length() < 10) {
        // no aceptar null, blancos o longitudes menores que 10 o mayores que 200
        throw new IllegalArgumentException("La descripcion debe estar entre 10 y 200 caracteres");
    } else {
        this.descripcion = descripcion;
    }
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
                ", valorHora=" + valorHora +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

}
