# simple-crm-backend

Java 21 Spring Boot REST API for customer and interaction management, backed by PostgreSQL. This repository completes **Module 4, Option 3**: Dockerize and deploy the Simple CRM backend instead of the TaskFlow API.

## Render Deployment

**Live API URL:** TODO: add the public Render URL after deployment.

The customer API requires a JWT. To verify the deployed service, log in at `/auth/login`, then call `/customers` with the returned bearer token:

```bash
curl -X POST https://TOBE-RENDER-SERVICE.onrender.com/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"p@55w0rd123"}'

curl https://TOBE-RENDER-SERVICE.onrender.com/customers \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

### Deployment evidence

Add the requested screenshots to the repository and update these links:

![Render dashboard showing an active deployment](screenshots/render-dashboard.png)

![Live Simple CRM API response](screenshots/live-api-response.png)

The screenshots are not included yet. Capture the Render service with a successful deployment and a browser or terminal response from the live API before submitting.

## API Endpoints

| Method | Path | Description | Authentication |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Authenticate and return a JWT | Public |
| `GET` | `/customers` | List customers | Bearer token |
| `POST` | `/customers` | Create a customer | Bearer token |
| `GET` | `/customers/{id}` | Get a customer | Bearer token |
| `PUT` | `/customers/{id}` | Update a customer | Bearer token |
| `DELETE` | `/customers/{id}` | Delete a customer | Bearer token |
| `GET` | `/customers/search?firstName=...` | Search customers by first name | Bearer token |
| `GET` | `/customers/{id}/interactions` | List a customer's interactions | Bearer token |
| `POST` | `/customers/{id}/interactions` | Add an interaction | Bearer token |

## Run Locally with Docker

Requirements: Docker with Compose. From the repository root, create a private local environment file and set a database password and JWT secret (at least 32 characters):

```bash
cp .env.example .env
```

Build the versioned API image and start it with PostgreSQL:

```bash
docker compose --env-file release.env --env-file .env up --build --wait
```

The local API listens on `http://localhost:8080`. Log in using the configured demo user's credentials and use the returned token to test the customer endpoint. Set the demo-user password through `APP_DEMO_USERS_USER_PASSWORD` in `.env` rather than relying on a default password.

Stop the containers with:

```bash
docker compose --env-file release.env --env-file .env down
```

The PostgreSQL data volume is preserved when the containers are removed. `.env` is ignored by Git; do not commit secrets.

## Deploy to Render

1. Push this repository to GitHub. In Render, create a **Web Service** from the repository and select **Docker** as the runtime. Render will build the root `Dockerfile`.
2. Create a Render Postgres database in the same region as the web service. Use its internal hostname, port, database name, username, and password for the API connection.
3. Add `APP_VERSION=0.1.0` to the web service's environment variables to match `RELEASE_VERSION` in `release.env`. Render makes this value available as the Docker build argument required by the Dockerfile.
4. Configure the web service with these runtime variables, using the Render Postgres connection values:

   | Variable | Value |
   | --- | --- |
   | `SPRING_PROFILES_ACTIVE` | `prod` |
  | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://INTERNAL_HOST:PORT/DATABASE` |
   | `SPRING_DATASOURCE_USERNAME` | PostgreSQL username |
   | `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password |
   | `JWT_SECRET` | A strong, unique secret of at least 32 characters |
   | `APP_DEMO_USERS_USER_PASSWORD` | A strong password for the demo user |
   | `APP_DEMO_USERS_ADMIN_PASSWORD` | A strong admin password |
   | `APP_DEMO_USERS_MANAGER_PASSWORD` | A strong manager password |
   | `CORS_ALLOWED_ORIGINS` | The deployed frontend origin, if applicable |

5. Use the public `onrender.com` URL assigned to the web service, then verify `/auth/login` and `/customers` as shown above. The app reads Render's `PORT` environment variable and uses `8080` as its local default.
6. Replace the live URL placeholder, add both screenshots under `screenshots/`, and push the updated README.

Keep production credentials in Render's environment settings, never in Git.

## Versioning

`release.env` defines `RELEASE_VERSION=0.1.0`. Docker Compose passes this value into the Docker build, and Maven uses it as the application artifact version. Update this value for future releases.

## Tests

The test profile uses an in-memory H2 database and does not require Docker or `.env`:

```bash
set -a
. ./release.env
set +a
./mvnw --batch-mode -Drevision="$RELEASE_VERSION" verify
```

## Assignment Status

- Dockerfile: present; uses a multi-stage Java 21 build and a non-root runtime user.
- Local Docker setup: Docker Compose starts the API with PostgreSQL.
- Render deployment URL: not recorded yet.
- Required deployment screenshots: not included yet.
- Errors or failures: none recorded in this README.
