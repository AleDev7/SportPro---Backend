package poo.model;

import java.io.IOException;

import org.json.JSONObject;

public class Piscina extends InstalacionDeportiva {

    private boolean olimpica;

    public Piscina() { 
        this.id = "P0000";
        this.ancho = 0;
        this.largo = 0;
        this.descripcion = "Piscina sin descripcion";
        this.olimpica = false;
        this.valorHora = 15000;
    }

    public Piscina(String id) {
        this.id = id;
     }

    public Piscina(String id, double ancho, double largo, String descripcion, boolean olimpica, double valorHora) {
        this.id = id;
        this.ancho = ancho;
        this.largo = largo;
        this.descripcion = descripcion;
        this.olimpica = olimpica;
        this.valorHora = valorHora;
     }
     
     public Piscina(JSONObject json) throws IOException {
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            // Generar 3 dígitos aleatorios para el ID con prefijo "PS"
            int numeros = (int)(Math.random() * 900) + 100; // genera un número entre 100 y 999
            this.id = "PS" + numeros;
        }
        this.ancho = json.getDouble("ancho");
        this.largo = json.getDouble("largo");
        if (json.has("area")) {
            this.area = json.getDouble("area");
        } else {
            this.area = getArea();
        }
        this.descripcion = json.getString("descripcion");
        this.valorHora = 15000;

        // Verifica si el JSON tiene el campo "olimpica" antes de asignarlo
        this.olimpica = json.has("olimpica") && json.getBoolean("olimpica");
    }

     @Override
     public JSONObject toJSONObject() {
         JSONObject json = new JSONObject();
         json.put("id", id);
         json.put("ancho", ancho);
         json.put("largo", largo);
         json.put(("area"), getArea());
         json.put("descripcion", descripcion);
         json.put("olimpica", olimpica);
         json.put("valorHora", valorHora);
         json.put("tipoInstalacion", getTipoInstalacion() ); // <-- Agregado aquí
         return json;
     }
     

    public Piscina(Piscina piscina) {
        this.id = piscina.id;
        this.ancho = piscina.ancho;
        this.largo = piscina.largo;
        this.descripcion = piscina.descripcion;
        this.olimpica = piscina.olimpica;
        this.valorHora = piscina.valorHora;
     }

    public void setOlimpica(boolean olimpica) {
        this.olimpica = olimpica;
     }

    public boolean getOlimpica() {
        return olimpica;
     }


    @Override
    public String getTipoInstalacion() {
        // devolver "Piscina olímpica" : "Piscina normal"
        if (olimpica = true ) {
            return "Piscina olimpica";
        } else {
            return "Piscina normal";
        }
    }

    

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
            "Ancho: " + ancho + "m\n" +
            "Largo: " + largo + "m\n" +
            "Descripción: " + descripcion + "\n" +
            "Tipo de instalación: " + getTipoInstalacion() + "\n" +
            "Valor por hora: $" + valorHora;
}

    @Override
    public Double getCostoFuncionamiento(JSONObject tarifasF) {
        tarifasF = tarifasF.getJSONObject("piscina");
        double quimicos = tarifasF.getDouble("Quimicos");
        double electricidad = tarifasF.getDouble("Electricidad");
        double agua = tarifasF.getDouble("Agua");
        double limpiezaM = tarifasF.getDouble("Limpieza_Mantenimiento");
        double value = quimicos + electricidad + agua + limpiezaM;
        if(getOlimpica()){
            double costoAdicional = value + value * 0.05;
            value = costoAdicional;
        }
        return value;
    };

    
}


