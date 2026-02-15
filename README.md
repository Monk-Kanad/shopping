# Shopping Site Microservices Architecture

A scalable microservices-based shopping platform built with modern Java technologies, featuring OAuth 2.0 authentication, service discovery, and load balancing.

## Architecture Overview

The application follows a microservices architecture pattern with the following key components:

- **Reactive API Gateway**: Entry point for all client requests
- **Auth Server**: OAuth 2.0 Authorization Server for secure authentication
- **Load Balancer**: Distributes traffic across service instances
- **Service Registry (Consul)**: Service discovery and health monitoring
- **Microservices**: 
  - User Service Group (multiple instances)
  - Order Service Group (multiple instances)
- **Database**: MongoDB for data persistence

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 4.0.2 | Application Framework |
| Spring Cloud Gateway | Latest | API Gateway |
| Spring Authorization Server | Latest | OAuth 2.0 Server |
| MongoDB | Latest | NoSQL Database |
| Consul | Latest | Service Discovery & Registry |
| Load Balancer | Built-in | Traffic Distribution |

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** - [Download JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven** or **Gradle** - Build tools
- **MongoDB** - [Download MongoDB](https://www.mongodb.com/try/download/community)
- **Consul** - Service discovery (installation steps below)

## Consul Installation & Setup (Windows)

### Step 1: Download Consul

1. Visit the [Consul Downloads page](https://www.consul.io/downloads)
2. Download the Windows binary (ZIP file)
3. Extract the ZIP file to a directory, e.g., `C:\Consul\`

### Step 2: Add Consul to System PATH

1. Open **System Properties** → **Environment Variables**
2. Under **System Variables**, find and select **Path**
3. Click **Edit** → **New**
4. Add the Consul directory path: `C:\Consul\`
5. Click **OK** to save

### Step 3: Verify Installation

Open Command Prompt and run:

```bash
consul --version
```

You should see output similar to:
```
Consul v1.x.x
```

### Step 4: Create Data Directory

Create a directory for Consul data storage:

```bash
mkdir C:\Consul\Data
```

### Step 5: Start Consul Server

Run the following command to start Consul in development mode:

```bash
consul agent -dev -data-dir="C:\Consul\Data" -ui
```

**Command Breakdown:**
- `agent` - Runs a Consul agent
- `-dev` - Runs in development mode (single-node)
- `-data-dir` - Specifies where Consul stores data
- `-ui` - Enables the web UI

### Step 6: Verify Consul is Running

1. **Check Console Output**: You should see logs indicating Consul is running
2. **Access Web UI**: Open browser and navigate to [http://localhost:8500](http://localhost:8500)
3. **API Check**: Run `curl http://localhost:8500/v1/catalog/services`

## Project Structure

```
shopping/
├── api-gateway/
│   ├── src/
│   └── pom.xml
├── authorization-server/
│   ├── src/
│   └── pom.xml
├── user-service/
│   ├── src/
│   └── pom.xml
├── order-service/
│   ├── src/
│   └── pom.xml
└── README.md
```


## Running the Application

### Step 1: Start Infrastructure Services

```bash
# Start MongoDB
mongod

# Start Consul (in a separate terminal)
consul agent -dev -data-dir="C:\Consul\Data" -ui
```

### Step 2: Start Microservices

Start each service in the following order:

```bash
# 1. Start Auth Server
cd authorization-server
mvn spring-boot:run

# 2. Start API Gateway
cd api-gateway
mvn spring-boot:run

# 3. Start User Service (multiple instances)
cd user-service
mvn spring-boot:run -Dserver.port=8081

# 4. Start Order Service (multiple instances)
cd order-service
mvn spring-boot:run -Dserver.port=8082
```

### Step 3: Verify Services in Consul

Open [http://localhost:8500/ui](http://localhost:8500/ui) and verify all services are registered and healthy.

## Testing the Application

### Obtain OAuth 2.0 Access Token

Use the following cURL command to obtain an access token from the Authorization Server:

```bash
curl -X POST http://localhost:9090/oauth2/token \
  -u api-gateway:secret123 \
  -d "grant_type=client_credentials" \
  -d "scope=read"
```

**Expected Response:**

```json
{
  "access_token": "eyJraWQiOiJmZmNkZmFmMi04YWViLTQ3OTYtYjU1OC1iZWJlODgwNjM4NjQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhcGktZ2F0ZXdheSIsImF1ZCI6ImFwaS1nYXRld2F5IiwibmJmIjoxNzcxMTc2Mjc4LCJzY29wZSI6WyJyZWFkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA5MC9pc3N1ZXIiLCJleHAiOjE3NzExNzY1NzgsImlhdCI6MTc3MTE3NjI3OCwianRpIjoiNTA0OTE4ZjctZTIyMS00NGYyLTg3ZjEtODE5NmJiNDM5Yzg4In0.PkkM1WhCOZ3K3YrTLSioAeQVzlihswwIaa8tEOjt1NwG0Z8zM98ipNdR1mVhkhaq0wGR46NUrPN6CcZgL2ua4oH30GZFwVhMQGRDmWOBaZuYw1kyNmXv0zwoXdjQ_ySUUKkSLwy2lpnwVkgTf4ZLNYuZy2MUQK4PNycYi3ngZkxbPKjXLonaXCAzNHnn0nRt_ETuMQhXkfpDZ88OBH3Lnvt52kfIUJDB0LJmZMVFp4Zti9byusW_DqTZ3ri8M_r6BsRx09soJT0G4AvlLKkUbQFSO6G7nnBgCZ_3FCIy86WmCXwcg6ClJ1RSUD4QIkwZl61OqMwSHyvtvGJMa1IPLg",
  "scope": "read",
  "token_type": "Bearer",
  "expires_in": 299
}
```

### Making Authenticated API Calls

Use the access token to make authenticated requests:

```bash
# Get Users
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Create Order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId": "123", "product": "Laptop", "quantity": 1}'
```

## Port Configuration

| Service | Default Port |
|---------|--------------|
| Auth Server | 9090 |
| API Gateway | 8080 |
| User Service | 8081 |
| Order Service | 8082 |
| Consul | 8500 |
| MongoDB | 27017 |

## Key Features

### 1. **Service Discovery**
- Automatic service registration with Consul
- Health checks and monitoring
- Dynamic service discovery

### 2. **Load Balancing**
- Client-side load balancing via Spring Cloud LoadBalancer
- Distributes traffic across multiple service instances
- Automatic failover handling

### 3. **Security**
- OAuth 2.0 Client Credentials flow
- JWT-based access tokens
- Secure service-to-service communication

### 4. **API Gateway**
- Single entry point for all client requests
- Routing and request forwarding
- Cross-cutting concerns (authentication, logging)

### 5. **Reactive Architecture**
- Non-blocking I/O with Spring WebFlux
- Better resource utilization
- Improved scalability

## Monitoring & Management

### Consul Dashboard

Access the Consul UI at [http://localhost:8500/ui](http://localhost:8500/ui) to:
- View registered services
- Check service health status
- Monitor service instances
- Access key-value store

### Spring Boot Actuator

Enable actuator endpoints in `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,consul
```

Access endpoints:
- Health: `http://localhost:PORT/actuator/health`
- Info: `http://localhost:PORT/actuator/info`
- Metrics: `http://localhost:PORT/actuator/metrics`

## Troubleshooting

### Consul Not Starting

**Issue**: Consul agent fails to start

**Solution**:
```bash
# Kill any existing Consul processes
taskkill /F /IM consul.exe

# Restart with fresh data directory
consul agent -dev -data-dir="C:\Consul\Data" -ui
```

### Services Not Registering with Consul

**Issue**: Services don't appear in Consul UI

**Solution**:
1. Check `application.yml` has correct Consul configuration
2. Verify Consul is running: `curl http://localhost:8500/v1/status/leader`
3. Check service logs for registration errors
4. Ensure `spring-cloud-starter-consul-discovery` dependency is included

### OAuth Token Request Fails

**Issue**: 401 Unauthorized when requesting token

**Solution**:
1. Verify client credentials: `api-gateway:secret123`
2. Check Auth Server is running on port 9090
3. Ensure Authorization Server is properly configured
4. Verify client registration in Auth Server configuration

### MongoDB Connection Issues

**Issue**: Services can't connect to MongoDB

**Solution**:
```bash
# Check MongoDB is running
mongo --eval "db.version()"

# Verify connection string in application.yml
# Ensure MongoDB port 27017 is not blocked by firewall
```

## Development Tips

### Running Multiple Service Instances

To test load balancing, run multiple instances of the same service:

```bash
# Terminal 1 - User Service Instance 1
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Terminal 2 - User Service Instance 2
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

### Hot Reload with Spring DevTools

Add Spring DevTools dependency for automatic application restarts during development:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
- Create an issue in the GitHub repository
- Check existing documentation
- Review Consul logs: `C:\Consul\Data\`

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [Consul Documentation](https://www.consul.io/docs)
- [MongoDB Documentation](https://docs.mongodb.com/)

---

**Made with ❤️ using Spring Boot and Microservices Architecture**
