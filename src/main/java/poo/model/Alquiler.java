package poo.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

import poo.helpers.Utils;

public class Alquiler {

    private String id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Socio socio;
    private InstalacionDeportiva instalacionDeportiva;

    public Alquiler() {
        // asigna un ID aleatorio, LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS)al inicio, 
        // LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS) al final
        // Un socio creado con el constructor por defecto y
        // Una CanchaMultiproposito
        this.id = "A0000";
        this.fechaHoraInicio = LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS);
        this.fechaHoraFin = LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS);
        this.socio = new Socio();
        this.instalacionDeportiva = new CanchaMultiproposito();
    }

    public Alquiler(String id, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Socio socio, InstalacionDeportiva instalacionDeportiva) { 
        this.id = id;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.socio = socio;
        this.instalacionDeportiva = instalacionDeportiva;
    }


    public Alquiler(JSONObject json) throws IOException {
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            this.id = Utils.getRandomKey(10); // o la longitud que desees
        }
        this.fechaHoraInicio = LocalDateTime.parse(json.getString("fechaHoraInicio"));
        this.fechaHoraFin = LocalDateTime.parse(json.getString("fechaHoraFin"));
        this.socio = new Socio(json.getJSONObject("socio")); 
        this.instalacionDeportiva = InstalacionDeportiva.getInstance(json.getJSONObject("instalacionDeportiva"));
    }

    public Alquiler(Alquiler alquiler) {
        this.id = alquiler.id;
        this.fechaHoraInicio = alquiler.fechaHoraInicio;
        this.fechaHoraFin = alquiler.fechaHoraFin;
        this.socio = alquiler.socio;
        this.instalacionDeportiva = alquiler.instalacionDeportiva;

     }

    public String getId() {
        return id;
     }

    public void setId(String id) {
        if (id.length() == 5) { 
        // el ID no puede ser nulo, estar en blanco o tener una longitud distinta de 5
        this.id = id;

        } else {
            throw new IllegalArgumentException("Se espera un id de 5 caracteres");
        }
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
     }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        if (fechaHoraInicio.isBefore(fechaHoraFin)) {  
        // lanzar NullPointerException si la fecha es nula
        // lanzar IllegalArgumentException si no se cumple que La fecha y hora de inicio debe ser menor que la fecha y hora de fin.
        // truncar a minutos
        this.fechaHoraInicio = fechaHoraInicio;
        } else {
            throw new IllegalArgumentException("La fecha y hora de inicio no puede ser superior a la final");
        }
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
     }

     public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        if (fechaHoraFin.isAfter(fechaHoraInicio)) {  
        // lanzar NullPointerException si la fecha es nula
        // lanzar IllegalArgumentException si no se cumple que La fecha y hora de inicio debe ser menor que la fecha y hora de fin.
        // truncar a minutos
        this.fechaHoraFin = fechaHoraFin;
        } else {
            throw new IllegalArgumentException("La fecha y hora fin no puede ser inferior a la inicial");
        }
    }

    public Socio getSocio() { 
        return socio;
    }

    public void setSocio(Socio socio) {
        if (socio != null) { 
        // el socio no puede ser null
        this.socio = socio;
        } else {
            throw new IllegalArgumentException("El socio no puede ser nulo");
        }
    }

    public InstalacionDeportiva getInstalacionDeportiva() { 
        return instalacionDeportiva;
     }

    public void setInstalacionDeportiva(InstalacionDeportiva instalacionDeportiva) {
        if (instalacionDeportiva != null) { 
        // la instalacionDeportiva no puede ser null
        this.instalacionDeportiva = instalacionDeportiva;
        } else {
            throw new IllegalArgumentException("La instalacion deportiva no puede ser nula");
        }
    }


    public long getHoras() { 
        return ChronoUnit.HOURS.between(fechaHoraInicio, fechaHoraFin);
    }

    public double getValorAlquiler() { 
        return getHoras() * instalacionDeportiva.getValorHora();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alquiler alquiler = (Alquiler) obj;
        return id.equals(alquiler.id) || 
               (fechaHoraInicio.equals(alquiler.fechaHoraInicio) &&
                fechaHoraFin.equals(alquiler.fechaHoraFin) &&
                socio.equals(alquiler.socio) &&
                instalacionDeportiva.equals(alquiler.instalacionDeportiva));
    }

    public JSONObject toJSONObject() { 
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("fechaHoraInicio", fechaHoraInicio.toString());
        json.put("fechaHoraFin", fechaHoraFin.toString());
        json.put("socio", socio.toJSONObject());
        json.put("instalacionDeportiva", instalacionDeportiva.toJSONObject());
        return json;
    }

    @Override
    public String toString() { 
        return "ID: " + id + "\n" +
               "Fecha y hora de inicio: " + fechaHoraInicio + "\n" +
               "Fecha y hora de fin: " + fechaHoraFin + "\n" +
               "Socio: " + socio + "\n" +
               "Instalación deportiva: " + instalacionDeportiva + "\n" +
               "Valor del alquiler: $" + getValorAlquiler();
    }

    
}
