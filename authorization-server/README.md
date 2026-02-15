### To fetch the access token use below curl
```shell
curl -X POST http://localhost:9090/oauth2/token \
  -u my-api-client:my-secret-key \
  -d "grant_type=client_credentials" \
  -d "scope=api.read"
```