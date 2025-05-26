package poo.model;

import java.util.Objects;

import org.json.JSONObject;

public class CanchaMultiproposito extends InstalacionDeportiva {

    private boolean graderia;

    public CanchaMultiproposito() {
        this.id = "M0001";
        this.ancho = 0;
        this.largo = 0;
        this.descripcion = "Cancha multiproposito sin descripcion";
        this.graderia = false;
        this.valorHora = 5000;
    }

    public CanchaMultiproposito(String id) {
        this.id = id;
     }

    public CanchaMultiproposito(String id, double ancho, double largo, String descripcion, boolean graderia, double valorHora) { 
        this.id = id;
        this.ancho = ancho;
        this.largo = largo;
        this.descripcion = descripcion;
        this.graderia = graderia;
        this.valorHora = valorHora;
    }

    public CanchaMultiproposito(JSONObject json) {
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            // Generar 3 dígitos aleatorios para el ID con prefijo "CM"
            int numeros = (int)(Math.random() * 900) + 100; // genera un número entre 100 y 999
            this.id = "CM" + numeros;
        }
        this.ancho = json.getDouble("ancho");
        this.largo = json.getDouble("largo");
        this.descripcion = json.getString("descripcion");
        this.valorHora = 5000;

        // Verifica si el JSON tiene el campo "graderia" antes de asignarlo
        this.graderia = json.has("graderia") && json.getBoolean("graderia");
    }

    public CanchaMultiproposito(CanchaMultiproposito multiproposito) { 
        this.id = multiproposito.id;
        this.ancho = multiproposito.ancho;
        this.largo = multiproposito.largo;
        this.descripcion = multiproposito.descripcion;
        this.graderia = multiproposito.graderia;
        this.valorHora = multiproposito.valorHora;
    }

    public String getStrGraderia() {
        // devuelve "Con graderia" o "Sin graderia"
        if (graderia) {
            return "con graderia";
        } else {
            return "sin graderia";
        }
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        super.setId(id);
    }

    public boolean getGraderia() {
        return graderia;
     }

    public void setGraderia(boolean graderia) { 
        // retorna una cadena indicando si tiene o no tiene gradería
        this.graderia = graderia;
    }

    @Override
    public Double getCostoFuncionamiento(JSONObject funcionamiento) {
        JSONObject funcionamientoMultiproposito = funcionamiento.getJSONObject("Canchas_multiproposito");

        double costoBase = valorHora;

        if (graderia) {
            double incrementa = funcionamientoMultiproposito.getDouble("Con_gradria");
            costoBase = costoBase * incrementa;
        } else {
            double incrementa = funcionamientoMultiproposito.getDouble("Sin_gradria");
            costoBase = costoBase * incrementa;
        }

        return costoBase;
    }

    

    @Override
    public String getTipoInstalacion() {
        return "Multiproposito" + " " + getStrGraderia();
    }

    @Override
public String toString() {
    return "ID: " + id + "\n" +
           "Ancho: " + ancho + "m\n" +
           "Largo: " + largo + "m\n" +
           "Descripción: " + descripcion + "\n" +
           "Tipo de instalacion: " + getTipoInstalacion() + "\n" +
           "Valor por hora: $" + valorHora;
}


    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("graderia", graderia);
        return json;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CanchaMultiproposito that = (CanchaMultiproposito) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
    
    


