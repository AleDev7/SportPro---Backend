package poo.helpers;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import io.javalin.http.Context;
import poo.services.Service;

public class Controller<T>{

    private final String currentPath;

    public Controller(final Service<T> service) {
        // se asigna el endPoint, la dirección específica a la que se envía una
        // solicitud,
        // para generalizar, se utiliza el nombre de la clase como endPoit
        currentPath = service.getDataType().getSimpleName();

        System.out.println("++++++" + currentPath);

        path(
                currentPath.toLowerCase(),
                () -> {

                    get("", ctx -> response(ctx, service.getAll()));

                    get(
                            "/indice/{param}", // buscar por el índice o posición en el array
                            ctx -> {
                                String arg = ctx.pathParam("param");
                                int i = Integer.parseInt(arg, 10);
                                response(ctx, service.get(i));
                            });

                    get(
                            "/id/{param}", // buscar por la llave primaria: id|nroGuia
                            ctx -> {
                                String arg = ctx.pathParam("param");
                                response(ctx, service.get(arg));
                            });

                    get("/conteo", ctx -> response(ctx, service.size()));

                    post("", ctx -> response(ctx, service.add(ctx.body())));

                    patch( // también hubiera podido ser put
                            "/{param}",
                            ctx -> {
                                String id = ctx.pathParam("param");
                                String newData = ctx.body();
                                response(ctx, service.update(id, newData));
                            });

                    delete("/{param}", ctx -> response(ctx, service.remove(ctx.pathParam("param"))));

                });

    }

    private Context response(@NotNull Context ctx, JSONObject json) {
        if (json == null) {
            ctx.status(404);
            json = new JSONObject().put("request", ctx.fullUrl()).put("error",
                    "La solicitud ha producido 'null' como respuesta");
        } else if (json.has("error")) {
            ctx.status(404);
            json.put("request", ctx.fullUrl());
        } else if (!json.has("message")) {
            json = new JSONObject().put("message", "ok").put("data", json);
        }
        return ctx.json(json.toString());
    }

    public void info() {
        System.out.println(
                String.format("%s>> Creados los endpoints para %sService%s", Color.BLUE, currentPath, Color.RESET));
  }
}