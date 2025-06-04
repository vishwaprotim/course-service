# course-service
A simple Spring Boot REST application with in-memory collections

## Launching the Application
### From your local system via IDE
Run the application from your IDE and access this API from here: [http://localhost:8080](http://localhost:8080)

### As a Docker Container

Create the image:
```shell
docker build .
```
Now create the container and run it:
```shell
docker build -t course-service:latest .
```
```shell
docker run -p 8080:8080 course-service:latest 
```

Once the container starts, you can access the API from here: [http://localhost:8080](http://localhost:8080)

### Creating K8 cluster with Minikube

- Start docker daemon/desktop
- Start minikube
```shell
minikube start
```
- Load the docker image you created above into minikube
```shell
minikube image load course-service:latest
```
- You can verify the images loaded into minikube here
```shell
minikube image ls --format table
|-----------------------------------------|----------|---------------|--------|
|                  Image                  |   Tag    |   Image ID    |  Size  |
|-----------------------------------------|----------|---------------|--------|
| docker.io/library/course-service        | 0.0.1    | 3e70f97eccd4f | 546MB  |
| docker.io/library/course-service        | latest   | 3e70f97eccd4f | 546MB  |
| registry.k8s.io/etcd                    | 3.5.16-0 | 7fc9d4aa817aa | 142MB  |
| registry.k8s.io/coredns/coredns         | v1.11.3  | 2f6c962e7b831 | 60.2MB |
| gcr.io/k8s-minikube/storage-provisioner | v5       | ba04bb24b9575 | 29MB   |
| registry.k8s.io/kube-apiserver          | v1.32.0  | 2b5bd0f16085a | 93.9MB |
| registry.k8s.io/kube-scheduler          | v1.32.0  | c3ff26fb59f37 | 67.9MB |
| registry.k8s.io/kube-controller-manager | v1.32.0  | a8d049396f6b8 | 87.2MB |
| registry.k8s.io/kube-proxy              | v1.32.0  | 2f50386e20bfd | 97.1MB |
| registry.k8s.io/pause                   | 3.10     | afb61768ce381 | 514kB  |
| docker.io/kubernetesui/dashboard        | <none>   | 20b332c9a70d8 | 244MB  |
| docker.io/kubernetesui/metrics-scraper  | <none>   | a422e0e982356 | 42.3MB |
|-----------------------------------------|----------|---------------|--------|
```
#### Creating a pod for your service
- Create a pod for your service
```shell
kubectl run course-service --image=course-service:latest
```
- You can check the pod status and events
```shell
kubectl get pods
NAME             READY   STATUS    RESTARTS   AGE
course-service   1/1     Running   0          6s

kubectl describe pods
Name:             course-service
Namespace:        default
Priority:         0
Service Account:  default
Node:             minikube/192.168.49.2
Start Time:       Wed, 04 Jun 2025 19:50:37 +0530
Labels:           run=course-service
...
...
...
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  2m56s  default-scheduler  Successfully assigned default/course-service to minikube
  Normal  Pulled     2m55s  kubelet            Container image "course-service:0.0.1" already present on machine
  Normal  Created    2m55s  kubelet            Created container: course-service
  Normal  Started    2m55s  kubelet            Started container course-service
```
- A more intuitive way to check the pod details and events is through the minikube dashboard
```shell
minikube dashboard
```
#### Creating pods using deployments

Apply the deployment and service files
```shell
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

- The application can be accesses inside the cluster from http://<host>:<service-port>/ 
- To get the host we need minikube ip
```shell
minikube ip
192.168.49.2
```
- To get the service port, run below command
```shell
kubectl get services
NAME             TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
course-service   NodePort    10.104.177.26   <none>        8080:31994/TCP   4m45s
kubernetes       ClusterIP   10.96.0.1       <none>        443/TCP          2d3h
```
Hence, in this case, the url will be http://192.168.49.2:31994

- However, this URL will not be accessible from outside the K8 cluster. To access our application, we need a tunnel port created by minikube.
- Luckily for us, minikube creates it for us when we launch our service
```shell
minikube service course-service
|-----------|----------------|-------------|---------------------------|
| NAMESPACE |      NAME      | TARGET PORT |            URL            |
|-----------|----------------|-------------|---------------------------|
| default   | course-service |        8080 | http://192.168.49.2:31994 |
|-----------|----------------|-------------|---------------------------|
🏃  Starting tunnel for service course-service.
|-----------|----------------|-------------|------------------------|
| NAMESPACE |      NAME      | TARGET PORT |          URL           |
|-----------|----------------|-------------|------------------------|
| default   | course-service |             | http://127.0.0.1:50524 |
|-----------|----------------|-------------|------------------------|
🎉  Opening service default/course-service in default browser...
❗  Because you are using a Docker driver on darwin, the terminal needs to be open to run it.
```
- If the browser does not open automatically, we can access our application at http://localhost:50524 or http://127.0.0.1:50524
- We can check the services running from here
```shell
minikube service list
|----------------------|---------------------------|--------------|-----|
|      NAMESPACE       |           NAME            | TARGET PORT  | URL |
|----------------------|---------------------------|--------------|-----|
| default              | course-service            |         8080 |     |
| default              | kubernetes                | No node port |     |
| kube-system          | kube-dns                  | No node port |     |
| kubernetes-dashboard | dashboard-metrics-scraper | No node port |     |
| kubernetes-dashboard | kubernetes-dashboard      | No node port |     |
|----------------------|---------------------------|--------------|-----|
```