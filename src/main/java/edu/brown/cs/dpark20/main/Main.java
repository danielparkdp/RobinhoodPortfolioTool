package edu.brown.cs.dpark20.main;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.dpark20.algorithms.Options;
import edu.brown.cs.dpark20.algorithms.Company;
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
    Spark.post("/refresh", new RefreshHandler());
    Spark.post("/company", new CompanyHandler());


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

      //List<String[]> portfolio = options.getOptions();
      //List<double[]> graph = options.getGraphPoints(portfolio);

      //TEST:
      String[] inpts = qm.value("inpts").split(",");
      List<String[]> input = processInput(inpts);

  		List<String[]> opts = options.getOptions(input, 0.00);
  		List<double[]> coords = options.getGraphPoints(opts);


      Map<String, Object> variables = ImmutableMap.of("portfolio", opts, "graphPoints", coords);
      return GSON.toJson(variables);
    }

  }


  private static class RefreshHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      QueryParamsMap qm = req.queryMap();

      String s = qm.value("portfolio");
      String[] split = s.split(",");
  		List<String[]> opts = new ArrayList<>();
      for (int i = 3; i < split.length; i+=4){
        opts.add(options.createOption(split[i-3], split[i-2], split[i-1], Double.parseDouble(split[i])));
      }

  		List<double[]> coords = options.getGraphPoints(opts);


      Map<String, Object> variables = ImmutableMap.of("graphPoints", coords);
      return GSON.toJson(variables);
    }

  }

  private static class CompanyHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      QueryParamsMap qm = req.queryMap();

      String s = qm.value("companyName");

      Company comp = new Company(s);

      Map<String, Object> variables = ImmutableMap.of("price", comp.getPrice(), "volatility", comp.getVolatility());
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

   private static List<String[]> processInput(String[] inpt){
     String[] low = new String[3];
     String[] mid = new String[3];
     String[] high = new String[3];
     // low[0] = "1";
     // low[1] = "10";
     // low[2] = "20";
     // mid[0] = "4";
     // mid[1] = "20";
     // mid[2] = "24.88";
     // high[0] = "3";
     // high[1] = "24.88";
     // high[2] = "29.12";

     low[0] = getNumString(inpt[0]);
     low[1] = inpt[1];
     low[2] = inpt[2];
     mid[0] = getNumString(inpt[3]);
     mid[1] = inpt[4];
     mid[2] = inpt[5];
     high[0] = getNumString(inpt[6]);
     high[1] = inpt[7];
     high[2] = inpt[8];

     List<String[]> input = new ArrayList<>();
     if (!low[0].equals("0")){
       input.add(low);
     }
     if (!mid[0].equals("0")){
       input.add(mid);
     }
     if (!high[0].equals("0")){
       input.add(high);
     }
     return input;
   }

   private static String getNumString(String s){
     if (s.equals("zero")){
       return "0";
     }
     else if (s.equals("one")){
       return "1";
     }
     else if (s.equals("two")){
       return "2";
     }
     else if (s.equals("three")){
       return "3";
     }
     else if (s.equals("four")){
       return "4";
     }
     else {
       return "5";
     }
   }


}
