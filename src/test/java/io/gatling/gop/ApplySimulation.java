package io.gatling.gop;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class ApplySimulation extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://onepoint.wd3.myworkdayjobs.com")
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*", ".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*", ".*www\\.google\\.com.*", ".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
    .acceptHeader("image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("fr,fr-FR;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0");
  
  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("Accept", "application/json"),
    Map.entry("Accept-Language", "fr-FR"),
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Content-Type", "application/json"),
    Map.entry("Origin", "https://onepoint.wd3.myworkdayjobs.com"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("X-CALYPSO-CSRF-TOKEN", "20a3d0a6-7378-4118-ab2e-cf81f68b0400"),
    Map.entry("sec-ch-ua", "Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_2 = Map.ofEntries(
    Map.entry("Accept", "application/json"),
    Map.entry("Accept-Language", "fr-FR"),
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Content-Type", "application/x-www-form-urlencoded"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("X-CALYPSO-CSRF-TOKEN", "20a3d0a6-7378-4118-ab2e-cf81f68b0400"),
    Map.entry("sec-ch-ua", "Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );


  private ScenarioBuilder scn = scenario("ApplySimulation")
    .exec(
      http("Récupération des jobs")
        .post("/wday/cxs/onepoint/onepoint/jobs")
        .headers(headers_1)
        .body(RawFileBody("io/gatling/gop/applysimulation/0001_request.json"))
    )
    .pause(7)
    .exec(
      http("Selection d'un job")
        .get("/wday/cxs/onepoint/onepoint/job/Data-Engineer_JR100115-1")
        .headers(headers_2)
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
