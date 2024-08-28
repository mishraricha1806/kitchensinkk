Kitchensink Application
This repository contains the Kitchensink Java Spring Boot application. This guide provides instructions for deploying the application using Docker and Kubernetes.

Prerequisites
Before you begin, ensure you have the following installed:

Java 21: Ensure you have Java 21 installed to build the application.
Maven: Used for building the application.
Docker: To containerize the application.
Kubernetes: For deploying the application to a Kubernetes cluster.
kubectl: Command-line tool for interacting with the Kubernetes cluster.
Building the Application
Clone the repository:

bash
Copy code
git clone <repository-url>
cd kitchensinkk
Build the application:

Run the following command to package the application using Maven:

bash
Copy code
mvn clean package -DskipTests
This command will generate a JAR file in the target directory.

Docker Deployment
To run the application locally using Docker, follow these steps:

Build the Docker Image:

bash
Copy code
docker build -t kitchensink-app:latest .
Run the Docker Container:

bash
Copy code
docker run -p 8080:8080 kitchensink-app:latest
The application will be available at http://localhost:8080.

Using Docker Compose
If you prefer to use Docker Compose for local development:

Run Docker Compose:

bash
Copy code
docker-compose up
This command will start the application, and it will be accessible at http://localhost:8080.

Kubernetes Deployment
To deploy the application on a Kubernetes cluster:

Build and Push Docker Image:

Before deploying to Kubernetes, ensure your Docker image is available in a container registry accessible by your Kubernetes cluster.

bash
Copy code
docker tag kitchensink-app:latest <your-docker-repo>/kitchensink-app:latest
docker push <your-docker-repo>/kitchensink-app:latest
Replace <your-docker-repo> with your Docker repository name.

Deploy to Kubernetes:

Apply the Kubernetes deployment and service files:

bash
Copy code
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
Access the Application:

For LoadBalancer Service: Run kubectl get services to get the external IP address assigned to the service. Access the application at http://<external-ip>.

For NodePort Service (if you use this type instead): Find the NodePort assigned by running kubectl get services and access the application at http://<node-ip>:<node-port>.

Cleaning Up
To stop and remove the Docker container, run:

bash
Copy code
docker-compose down
To delete the Kubernetes deployment and service, run:

bash
Copy code
kubectl delete -f deployment.yaml
kubectl delete -f service.yaml
Troubleshooting
Check Logs: For Docker, you can view logs using docker logs <container-id>. For Kubernetes, use kubectl logs <pod-name>.
Common Errors: Ensure all dependencies are correctly set up, and Docker/Kubernetes commands have proper permissions.Steps to Use These Files:
Build Docker Image:

Run docker build -t your-docker-repo/kitchensink-app:latest . in the root directory of your project to build the Docker image.
Run Docker Compose (Optional):

Run docker-compose up to start the application locally with Docker Compose.
Deploy to Kubernetes:

Apply the Kubernetes configuration files with:
Copy code
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
Ensure you replace your-docker-repo/kitchensink-app:latest with the actual path to your Docker repository.