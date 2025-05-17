package poo.model;

import org.json.JSONObject;

import poo.helpers.Utils;

public class CanchaTenis extends InstalacionDeportiva {

    private TipoCancha tipoCancha;

    public CanchaTenis() { 
        this.id = "T0000";
        this.ancho = 0;
        this.largo = 0;
        this.descripcion = "Cancha de tenis sin descripcion";
        this.tipoCancha =  TipoCancha.OTROS;
        this.valorHora = 7000;
    }

    public CanchaTenis(String id) {
        this.id = id;
     }

    public CanchaTenis(String id, double ancho, double largo, String descripcion, TipoCancha tipo, double valorHora) { 
        this.id = id;
        this.ancho = ancho;
        this.largo = largo;
        this.descripcion = descripcion;
        this.tipoCancha = tipo;
        this.valorHora = valorHora;
    }   

    public CanchaTenis(JSONObject json) {
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            this.id = Utils.getRandomKey(10); // o la longitud que desees
        }
        this.ancho = json.getDouble("ancho");
        this.largo = json.getDouble("largo");
        this.descripcion = json.getString("descripcion");
        this.valorHora = 7000;
        
        // Verifica si el JSON tiene el campo "tipoCancha" antes de asignarlo
        if (json.has("tipoCancha")) {
            this.tipoCancha = TipoCancha.valueOf(json.getString("tipoCancha").toUpperCase());
        } else {
            this.tipoCancha = TipoCancha.OTROS; // Valor por defecto
        }
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        super.setId(id);
    }
    

    public CanchaTenis(CanchaTenis canchaTenis) {
        this.id = canchaTenis.id;
        this.ancho = canchaTenis.ancho;
        this.largo = canchaTenis.largo;
        this.descripcion = canchaTenis.descripcion;
        this.tipoCancha = canchaTenis.tipoCancha;
        this.valorHora = canchaTenis.valorHora;
     }

     public void setTipoCancha(TipoCancha tipo) {
        this.tipoCancha = tipo;
    }

    // Overload para aceptar String
    public void setTipoCancha(String tipoStr) {
        this.tipoCancha = TipoCancha.getEnum(tipoStr);
    }

    
    public TipoCancha getTipoCancha() {
        return tipoCancha;
    }
    @Override
    public Double getCostoFuncionamiento(JSONObject funcionamiento) {
        JSONObject funcionamientoTenis = funcionamiento.getJSONObject("Canchas_tenis");

       Double electricidad = funcionamientoTenis.getDouble("Electricidad");

        double costoBase = electricidad;

        if (tipoCancha == TipoCancha.CESPED) {
            Double ladrillo = funcionamientoTenis.getDouble("Ladrillo");
            costoBase += ladrillo;
        } else {
            Double cesped = funcionamientoTenis.getDouble("Cesped");
            costoBase += cesped;
        }
        return costoBase;
    }

    @Override
    public String getTipoInstalacion() {
        return tipoCancha.toString();
    }

    @Override
    public String toString() {
        String dato = String.format("id: %s%nDescripcion: %s%nTipoCancha: %s%nAncho: %s%nLargo: %s", id, descripcion,
                tipoCancha, ancho, largo);
        return dato;

    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("tipoCancha", tipoCancha.toString());
        return json;
    }

}
