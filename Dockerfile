# Use an official Maven runtime as a parent image
FROM maven:3.8.4-openjdk-11

# Set the working directory in the container
WORKDIR /app

# Copy the entire Gatling project directory into the container
COPY . /app

# Set the default command to run Gatling tests
CMD ["mvn", "gatling:test", "-Dgatling.simulationClass=io.gatling.gop.ConnectionSimulation", "-DGRAPHITE_HOST=influxdb", "-DGRAPHITE_PORT=2003", "-DSCRIPT_NAME=test"]