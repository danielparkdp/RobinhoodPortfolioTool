package edu.brown.cs.dpark20.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.dpark20.algorithms.Options;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import com.google.gson.Gson;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private static Options options;


  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);



    runSparkServer();



  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static void runSparkServer() {
      Spark.port(getHerokuAssignedPort());
    Spark.externalStaticFileLocation("src/main/resources/static");

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
    Spark.post("/search", new SubmitHandler());



  }

  /**
   * Handle requests to the front page of website.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title", "Options and Stock Portfolio Strategy");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private static class SubmitHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      QueryParamsMap qm = req.queryMap();
      options = new Options(qm.value("companyname"), qm.value("currPrice"), qm.value("interest"), qm.value("volatility"), qm.value("time"));

      Map<String, Object> variables = ImmutableMap.of("results", "temporary");
      return GSON.toJson(variables);
    }

  }

  static int getHerokuAssignedPort() {
      ProcessBuilder processBuilder = new ProcessBuilder();
      if (processBuilder.environment().get("PORT") != null) {
          return Integer.parseInt(processBuilder.environment().get("PORT"));
      }
      return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
   }


}
