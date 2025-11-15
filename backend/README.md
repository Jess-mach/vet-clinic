sudo docker run -d   --name postgres   -e POSTGRES_PASSWORD=morangos   -e POSTGRES_USER=jess   -e POSTGRES_DB=appdb   -p 5432:5432   ^Cstgres:16


lsof -ti:8080 | xargs kill -9

```bash
/usr/bin/env /usr/lib/jvm/java-21-openjdk-amd64/bin/java @/tmp/cp_6fjog0vdhpvissoy16sjj0sd3.argfile syscecilia.vet.SysCecilia.SysCeciliaApplication
```


lsof -i :8080 || netstat -tuln | grep 8080 || ss -tuln | grep 8080


# Primeiro, encontre o PID
lsof -ti :8080

# Depois mate o processo
kill <PID>

