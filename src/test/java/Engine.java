import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import io.gatling.gop.ConnectionSimulation;
import io.gatling.gop.GopWorkday;

public class Engine {

  public static void main(String[] args) {
    GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
      .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString())
      .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
      .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString())
            .simulationClass(GopWorkday.class.getName()).simulationClass(ConnectionSimulation.class.getName());

    Gatling.fromMap(props.build());
  }
}
