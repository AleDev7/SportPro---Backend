package poo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.Alquiler;
import poo.model.InstalacionDeportiva;
import poo.model.Socio;

public class AlquilerService implements Service<Alquiler> {

    private List<Alquiler> list;
    private final String fileName;
    private final Service<Socio> socioService;
    private final List<InstalacionDeportiva> instalaciones;

    public AlquilerService(Service<Socio> socioService, List<InstalacionDeportiva> instalaciones) throws Exception {
        this.socioService = socioService;
        this.instalaciones = instalaciones;
        this.fileName = Utils.getConfig().getJSONObject("archivos").getString("alquileres");

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        Alquiler alquiler = dataToAddOk(strJson);

        if (list.contains(alquiler)) {
            throw new Exception("El alquiler ya existe");
        }

        list.add(alquiler);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", alquiler.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Alquiler alquiler = getItem(id);
        if (alquiler == null) {
            throw new NoSuchElementException("No se encontró un alquiler con ID: " + id);
        }
        return alquiler.toJSONObject();
    }

    @Override
    public Alquiler getItem(String id) throws Exception {
        for (Alquiler a : list) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public JSONObject getAll() {
        JSONArray data = new JSONArray();
        for (Alquiler a : list) {
            data.put(a.toJSONObject());
        }
        return new JSONObject().put("message", "ok").put("data", data);
    }

    @Override
    public List<Alquiler> load() throws Exception {
        list = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(new Alquiler(jsonObj));
        }
        return list;
    }


    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        JSONObject newData = new JSONObject(strJson);
        Alquiler current = getItem(id);
        if (current == null) {
            throw new NoSuchElementException("No se encontró el alquiler con ID: " + id);
        }
        int i = list.indexOf(current);
        Alquiler updated = getUpdated(newData, current);
        list.set(i, updated);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", updated.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        Alquiler alquiler = getItem(id);
        if (alquiler == null) {
            throw new NoSuchElementException("No existe un alquiler con ID: " + id);
        }
        if (!list.remove(alquiler)) {
            throw new Exception("No se pudo eliminar el alquiler");
        }
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", alquiler.toJSONObject());
    }

    @Override
    public Alquiler dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);

        Socio socio = socioService.getItem(json.getString("socio"));
        if (socio == null) {
            throw new Exception("El socio no existe: " + json.getString("socio"));
        }

        InstalacionDeportiva inst = getInstalacion(json.getString("instalacionDeportiva"));
        if (inst == null) {
            throw new Exception("No se encontró la instalación con ID: " + json.getString("instalacionDeportiva"));
        }

        json.put("socio", socio.toJSONObject());
        json.put("instalacionDeportiva", inst.toJSONObject());

        return new Alquiler(json);
    }

    @Override
    public Alquiler getUpdated(JSONObject newData, Alquiler current) throws Exception {
        newData.put("id", current.getId()); // mantener el ID actual
        return dataToAddOk(newData.toString());
    }

    @Override
    public JSONObject size() {
        return new JSONObject().put("size", list.size()).put("message", "ok");
    }

    @Override
    public Class<Alquiler> getDataType() {
        return Alquiler.class;
    }

    private InstalacionDeportiva getInstalacion(String id) {
        for (InstalacionDeportiva i : instalaciones) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }
}
