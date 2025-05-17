package poo;

import java.util.Locale;

import io.javalin.Javalin;
import io.javalin.util.JavalinLogger;
import poo.helpers.Color;
import poo.helpers.Controller;
import poo.helpers.Utils;
import poo.model.Alquiler;
import poo.model.CanchaMultiproposito;
import poo.model.CanchaTenis;
import poo.model.InstalacionDeportiva;
import poo.model.Piscina;
import poo.model.Socio;
import poo.model.TipoCancha;
import poo.services.AlquilerService;
import poo.services.InstalacionDeportivaService;
import poo.services.Service;
import poo.services.SocioService;

public class App {

    public static void main(String[] args) throws Exception {
        int port = 7070;
        // https://javalin.io/tutorials/javalin-logging#using-any-of-those-loggers
        String message = String.format(
                "%sIniciando la API Rest de Envios y bodegaje. Use Ctrl+C para detener la ejecución%s", Color.CYAN,
                Color.RESET);
        JavalinLogger.info(message);

        Utils.trace = true; // no deshabilite la traza de errores hasta terminar completamente la aplicación

        if (Utils.trace) {
            // ver para tiempo de desarrollo: ./.vscode/launch.json
            JavalinLogger.info(String.format("%sHabilitada la traza de errores%s", Color.YELLOW, Color.RESET));
        } else {
            JavalinLogger.info(
                    String.format("%sEnvíe un argumento true|false para habilitar|deshabilitar la traza de errores%s",
                            Color.YELLOW, Color.RESET));
        }

        int length = args.length;
        if (length > 0) {
            Utils.trace = Boolean.parseBoolean(args[0]);
            if (length >= 2) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    JavalinLogger.info(String.format("%sSe esperaban los argumentos [true|false] [port]%s", Color.RED,
                            Color.RESET));
                }
            }
        }

        // estandarizar el formato monetario con separador de punto decimal, no con coma
        Locale.setDefault(Locale.of("es_CO"));

        Service<Socio> socioService = new SocioService();
        Service<InstalacionDeportiva> piscinaService = new InstalacionDeportivaService(Piscina.class);
        Service<InstalacionDeportiva> tenisService = new InstalacionDeportivaService(CanchaTenis.class);
        Service<InstalacionDeportiva> multipropositoService = new InstalacionDeportivaService(
                CanchaMultiproposito.class);
        Service<Alquiler> alquileresService = new AlquilerService(socioService,
                InstalacionDeportivaService.collectAll());

        Javalin
                .create(config -> {
                    config.http.defaultContentType = "application/json";
                    // ver https://javalin.io/plugins/cors#getting-started
                    config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));

                    config.router.apiBuilder(() -> {
                        new Controller<>(socioService).info();
                        new Controller<>(piscinaService).info();
                        new Controller<>(tenisService).info();
                        new Controller<>(multipropositoService).info();
                        new Controller<>(alquileresService).info();
                    });

                })
                .start(port)
                .get("/", ctx -> ctx.json(
                        "{ \"data\": \"Bienvenido al servicio de alquiler de instalaciones deportivas\", \"message\": \"ok\" }"))
                .get("/canchas/tipos", ctx -> ctx.json(TipoCancha.getAll().toString()))
                .get("/canchas/todas", ctx -> ctx.json(InstalacionDeportivaService.loadAll().toString()))
                .exception(
                        Exception.class,
                        (e, ctx) -> {
                            System.out.println(">>>>" + ctx.status());
                            Utils.printStackTrace(e);
                            String error = Utils.keyValueToStrJson(
                                    "error", e.getClass().getSimpleName(),
                                    "message", e.getMessage(),
                                    "request", ctx.fullUrl());
                            ctx.json(error).status(400);
                        });
        // .error(404, "json", ctx -> {
        //     JavalinLogger.info(String.format("%sNo se encontró %s%s", Color.YELLOW, ctx.fullUrl(), Color.RESET));
        //     String error = Utils.keyValueToStrJson("error", "404", "message", "Endpoint no encontrado", "request", ctx.fullUrl());
        //     ctx.json(error);
        // });

    }

}