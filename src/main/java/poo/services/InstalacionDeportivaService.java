package poo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.CanchaMultiproposito;
import poo.model.CanchaTenis;
import poo.model.InstalacionDeportiva;
import poo.model.Piscina;

public class InstalacionDeportivaService implements Service<InstalacionDeportiva> {

    private List<InstalacionDeportiva> list;
    private final String fileName;
    private final Class<InstalacionDeportiva> c;

    @SuppressWarnings("unchecked")
    public InstalacionDeportivaService(Class<? extends InstalacionDeportiva> c) throws Exception {
        this.c = (Class<InstalacionDeportiva>) c;

        if (Piscina.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig().getJSONObject("archivos").getString("piscina");
        } else if (CanchaTenis.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig().getJSONObject("archivos").getString("tenis");
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig().getJSONObject("archivos").getString("multiproposito");
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva conocida");
        }

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        InstalacionDeportiva inst = dataToAddOk(strJson);

        if (list.contains(inst)) {
            throw new Exception("Ya existe una instalación con ese ID");
        }

        list.add(inst);
        Utils.writeJSON(list, fileName);

        return new JSONObject().put("message", "ok").put("data", inst.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        InstalacionDeportiva inst = getItem(id);
        if (inst == null) {
            throw new NoSuchElementException("No se encontró la instalación con ID: " + id);
        }
        return inst.toJSONObject();
    }

    @Override
    public InstalacionDeportiva getItem(String id) throws Exception {
        InstalacionDeportiva temp = c.getConstructor(String.class).newInstance(id);
        int index = list.indexOf(temp);
        return index >= 0 ? list.get(index) : null;
    }

    @Override
    public JSONObject getAll() {
        JSONArray data = new JSONArray();
        for (InstalacionDeportiva inst : list) {
            data.put(inst.toJSONObject());
        }
        return new JSONObject().put("message", "ok").put("data", data);
    }

    @Override
    public List<InstalacionDeportiva> load() throws Exception {
        list = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(InstalacionDeportiva.getInstance(jsonObj));
        }

        return list;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        JSONObject newData = new JSONObject(strJson);
        InstalacionDeportiva current = getItem(id);

        if (current == null) {
            throw new NoSuchElementException("No existe la instalación con ID: " + id);
        }

        int index = list.indexOf(current);
        InstalacionDeportiva updated = getUpdated(newData, current);
        list.set(index, updated);
        Utils.writeJSON(list, fileName);

        return new JSONObject().put("message", "ok").put("data", updated.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        InstalacionDeportiva inst = getItem(id);
        if (inst == null) {
            throw new NoSuchElementException("No se puede eliminar. No existe una instalación con ID: " + id);
        }

        if (!list.remove(inst)) {
            throw new Exception("No se pudo eliminar la instalación con ID: " + id);
        }

        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", inst.toJSONObject());
    }

    @Override
    public InstalacionDeportiva dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        return InstalacionDeportiva.getInstance(json);
    }

    @Override
    public InstalacionDeportiva getUpdated(JSONObject newData, InstalacionDeportiva current) throws Exception {
        InstalacionDeportiva updated = InstalacionDeportiva.getInstance(newData);
        updated.setId(current.getId()); // El ID no se debe cambiar
        return updated;
    }

    @Override
    public JSONObject size() {
        return new JSONObject().put("size", list.size()).put("message", "ok");
    }

    @Override
    public Class<InstalacionDeportiva> getDataType() {
        return c;
    }

    public static List<InstalacionDeportiva> collectAll() throws Exception {
        List<InstalacionDeportiva> all = new ArrayList<>();
        all.addAll(new InstalacionDeportivaService(Piscina.class).load());
        all.addAll(new InstalacionDeportivaService(CanchaTenis.class).load());
        all.addAll(new InstalacionDeportivaService(CanchaMultiproposito.class).load());
        return all;
    }

    public static JSONObject loadAll() throws Exception {
        JSONArray data = new JSONArray();
        for (InstalacionDeportiva inst : collectAll()) {
            data.put(inst.toJSONObject());
        }
        return new JSONObject().put("message", "ok").put("data", data);
    }

    public static InstalacionDeportiva getInstalacion(String id) throws Exception {
        for (InstalacionDeportiva inst : collectAll()) {
            if (inst.getId().equals(id)) {
                return inst;
            }
        }
        return null;
    }
}
