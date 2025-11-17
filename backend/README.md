# 1 Primeiro Uso
```shell
sudo docker run -d   --name postgres   -e POSTGRES_PASSWORD=postgresql   -e POSTGRES_USER=postgresql   -e POSTGRES_DB=postgresql   -p 5432:5432  
```

lsof -ti:8080 | xargs kill -9

```bash
/usr/bin/env /usr/lib/jvm/java-21-openjdk-amd64/bin/java @/tmp/cp_6fjog0vdhpvissoy16sjj0sd3.argfile syscecilia.vet.SysCecilia.SysCeciliaApplication
```


# 2 - Para iniciar a aplicação
docker ps -a
docker start 9b4

```shell
cd /media/jess/WORKSPACE/win/jessica/workspace/SysCecilia/backend
mvn spring-boot:run


cd /media/jess/WORKSPACE/win/jessica/workspace/SysCecilia/vet-clinic
npm run dev


```

lsof -i :8080 || netstat -tuln | grep 8080 || ss -tuln | grep 8080


# Primeiro, encontre o PID
lsof -ti :8080

# Depois mate o processo
kill <PID>

http://localhost:8080/swagger-ui.html