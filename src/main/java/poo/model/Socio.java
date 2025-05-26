package poo.model;

import java.util.Objects;

import org.json.JSONObject;


public class Socio {

    private String id;
    private String nombre;
    private String direccion;
    private String telefono;

    public Socio() {
        // asignar ID de 12 caracteres, NN, "Dirección no registrada" y "Teléfono no registrado"
        this.id = "CXXXX";
        this.nombre = "NN";
        this.direccion = "Direccion no registrada";
        this.telefono = "Telefono no registrado";
    }

    public Socio(String id) { 
        this.id = id;
    }

    public Socio(String id, String nombre, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
     }

     public Socio(JSONObject json) {
        if (json.has("id")) {
            this.id = json.getString("id");
        } else {
            // Generar 3 dígitos aleatorios
            int numeros = (int)(Math.random() * 900) + 100; // genera un número entre 100 y 999
            this.id = "SC" + numeros;
        }
        this.nombre = json.optString("nombre", "NN");
        this.direccion = json.optString("direccion", "Direccion no registrada");
        this.telefono = json.optString("telefono", "Telefono no registrado");
    }

    public Socio(Socio s) {
        this.id = s.id;
        this.nombre = s.nombre;
        this.direccion = s.direccion;
        this.telefono = s.telefono;

     }

    //MUTADORES Y ACCESORES

    public void setId(String id) {
        if (id == null || id.length() != 12) {
        // no aceptar null, blancos y longitudes distintas a 12 caracteres
            throw new IllegalArgumentException("Se espera un ID de 12 caracteres");
        }
        else {
             this.id = id;
        }
    }

    public String getId() {
        return id;
    }


    public void setNombre(String nombre) {
        if (nombre == null || nombre.length() > 50 || nombre.length() < 1) {
        // el nombre no puede ser null o estar en blanco
            throw new IllegalArgumentException("Se espera un nombre entre 1 y 50 caracteres");
        }
        else {
            this.nombre = nombre;
        }
    }

    public String getNombre() { 
        return nombre;
    }


    public void setDireccion(String direccion) {
        if (direccion == null || direccion.length() < 10 || direccion.length() > 200) { 
        //  no puede ser null, blancos, menor que 10 caracteres ni mayor a 200
        //  eliminar los posibles blancos al principio y/o al final
        throw new IllegalArgumentException("Se espera una direccion entre 10 y 200 caracteres");
        }
        else {
            this.direccion = direccion;
        }
    }

    public String getDireccion() { 
        return direccion;
    }


    public void setTelefono(String telefono) {
        if (telefono == null || telefono.length() < 10 || telefono.length() > 100) { 
        //  no puede ser null, blancos, menor que 10 caracteres ni mayor a 100
        //  eliminar los posibles blancos al principio y/o al final. Se aceptan varios?
        throw new IllegalArgumentException("Se espera un telefono entre 10 y 100 caracteres");
        }
        else {
            this.telefono = telefono;
        }
    }

    public String getTelefono() { 
        return telefono;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Socio socio = (Socio) obj;
        return this.id.equals(socio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nombre", nombre);
        json.put("direccion", direccion);
        json.put("telefono", telefono);
        return json;
    }

    @Override
    public String toString() {
        return "Socio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
