# 🍪 Central Authentication – Manual Auth & Client Flow

This document captures the tested authentication flows for local development using curl, including user login, token generation, and client registration.

---

## 🧍 1. Login Endpoint (Custom)

### 🔗 `POST /login`

Used for manual login against internal user DB.

#### 📦 Request

```bash
curl -v -X POST http://localhost:9000/login \
  -H "Content-Type: application/json" \
  -d '{"username": "cookie", "password": "123456"}'
```
---  

### ✅ Expected Outcome

200 OK if credentials are correct.

Likely sets a JSESSIONID cookie.

## 🔐 2. OAuth2 Token Endpoint

### 🔗 `POST /oauth2/token`
⚠️ Used grant_type=password – Deprecated, replace ASAP
📜 With Basic Auth (client credentials in header)

```bash
curl -v -X POST http://localhost:9000/oauth2/token \
  -u 'my-client-id:my-client-secret' \
  -d 'grant_type=password&username=usuarito&password=123456&scope=openid'
```

🧪 Without Basic Auth (client credentials in body)

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -d 'grant_type=password&username=cookie&password=123456&scope=openid'
```

### ✅ Expected Outcome
Returns access token (JWT or Opaque depending on config)

200 OK with JSON payload

### ❌ What to fix

```bash
grant_type=password is insecure.
Use authorization_code with PKCE instead (see below ⬇️).
```

## 👑 3. Client Registration via Admin Endpoint

#### 🔗 `POST /admin/clients`

Used to create OAuth2 clients dynamically.

#### 📦 Request

```bash
curl -v -X POST http://localhost:9000/admin/clients \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "my-test-app-2",
    "clientSecret": "super-secret-2",
    "redirectUris": ["http://localhost:8081/login/oauth2/code/my-test-app"],
    "scopes": ["openid", "read", "write"],
    "grantTypes": ["authorization_code", "refresh_token"]
  }'
```

#### ⚠️ Initial Problem

HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer
No admin user existed → couldn't authenticate

Fixed by temporarily disabling security on /admin/** and ignoring CSRF

#### ✅ Final Outcome
Client successfully registered:

```
{
  "id": null,
  "clientId": "my-test-app-2",
  "clientSecret": "super-secret-2",
  "redirectUris": ["http://localhost:8081/login/oauth2/code/my-test-app"],
  "scopes": ["read", "openid", "write"],
  "grantTypes": ["refresh_token", "authorization_code"]
}
```

### 🛠️ Admin Bootstrapping Notes
Temporarily applied:

```
.requestMatchers("/admin/**").permitAll()
.csrf(csrf -> csrf.ignoringRequestMatchers("/admin/**"))
```