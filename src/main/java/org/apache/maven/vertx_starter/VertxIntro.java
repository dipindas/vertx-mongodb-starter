package org.apache.maven.vertx_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.json.JsonObject;

public class VertxIntro extends AbstractVerticle {
	public static final String COLLECTION = "democollection";
	private MongoClient mongo;
 
  @Override
  public void start(Future<Void> fut) {
	  
	  JsonObject config = Vertx.currentContext().config();
	  
	  String uri = config.getString("mongo_uri");
	    if (uri == null) {
	      uri = "mongodb://localhost:27017";
	    }
	    String db = config.getString("demodb");
	    if (db == null) {
	      db = "democollection";
	    }

	    JsonObject mongoconfig = new JsonObject()
	        .put("connection_string", uri)
	        .put("db_name", db);
	    
	  
	  mongo = MongoClient.createShared(vertx, mongoconfig);

	  createSomeData(fut);
	  
	  Router router= Router.router(vertx);
	  
	  router.route("/").handler(routingContext -> {
	      HttpServerResponse response = routingContext.response();
	      response
	          .putHeader("content-type", "text/html")
	          .end("<h1>Hello from my first Vert.x application</h1>");
	    });
	  router.get("/vertex/handler").handler(this::getAll);

	  
 
	  // Use minimum of JRE 1.8
	  
    vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
  }
  
  
  private void getAll(RoutingContext routingContext) {
	  HttpServerResponse response = routingContext.response();
      response
          .putHeader("content-type", "text/html")
          .end("<h1>This is a handler example</h1>");
    }
  
  private void createSomeData(Future<Void> fut) {
	    Employee employee1 = new Employee(1234567, "John", "ENG");
	    Employee employee2 = new Employee(12345678, "Sam", "DNA");
	    System.out.println(employee1.toJson());

	    // Do we have data in the collection ?
	    mongo.count(COLLECTION, new JsonObject(), count -> {
	      if (count.succeeded()) {
	        if (count.result() == 0) {
	          mongo.insert(COLLECTION, employee1.toJson(), ar -> {
	            if (ar.failed()) {
	              fut.fail(ar.cause());
	            } else {
	              mongo.insert(COLLECTION, employee2.toJson(), ar2 -> {
	                if (ar2.failed()) {
	                  fut.failed();
	                }
	              });
	            }
	          });
	        }
	      } else {
	        // report the error
	        fut.fail(count.cause());
	      }
	    });
	  }
  
}

