# Use the official InfluxDB 2.0 image as the base image
FROM influxdb:latest

# Set environment variables
ENV DOCKER_INFLUXDB_INIT_MODE=setup
ENV DOCKER_INFLUXDB_INIT_USERNAME=mdd
ENV DOCKER_INFLUXDB_INIT_PASSWORD=12345678
ENV DOCKER_INFLUXDB_INIT_ORG=mdd
ENV DOCKER_INFLUXDB_INIT_BUCKET=mdd_bucket

# Create volume mappings
VOLUME /var/lib/influxdb2 /etc/influxdb2

# Expose the InfluxDB 2.0 API and UI ports
EXPOSE 8086 8082

# Start the InfluxDB entry point
CMD ["influxd"]

# docker build -t influxdb_mdd_img .
# docker run -p 8086:8086 --name influxdb influxdb_mdd_img