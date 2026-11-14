# Simple CRM post-deployment API tests

This directory is a standalone [Bruno](https://www.usebruno.com/) collection for smoke-testing the Simple CRM API after deployment. It does not participate in the application build or runtime. Deleting `apitest/` removes the test collection without affecting the backend.

The collection logs in, creates a uniquely named customer, reads it, lists customers, updates it, searches for it, deletes it, and verifies that deletion. The JWT and created customer ID exist only as runtime variables.

## Prerequisites

- Bruno desktop, or Bruno CLI (`bru`)
- The demo user's deployed password
- Network access to the selected API

No real password is stored in this collection. `CHANGE_ME` is intentionally committed as a non-secret placeholder.

## Run against Render with Bruno CLI

From this directory, put the password in a temporary shell environment variable and override the collection placeholder:

```bash
export CRM_API_TEST_PASSWORD='your-demo-user-password'
bru run --env Render --env-var userPassword="$CRM_API_TEST_PASSWORD"
unset CRM_API_TEST_PASSWORD
```

The default Render target is:

```text
https://simple-crm-backend-htvk.onrender.com
```

To override the URL for another Render service or preview deployment:

```bash
bru run --env Render \
  --env-var baseUrl="https://your-service.onrender.com" \
  --env-var userPassword="$CRM_API_TEST_PASSWORD"
```

Render may take time to wake a sleeping service. If the first request times out during a cold start, wait for the service to become healthy and rerun the collection.

## Run locally

```bash
export CRM_API_TEST_PASSWORD='your-local-demo-user-password'
bru run --env Local --env-var userPassword="$CRM_API_TEST_PASSWORD"
unset CRM_API_TEST_PASSWORD
```

## Run in Bruno desktop

1. Open Bruno and choose **Open Collection**.
2. Select this `apitest/` directory.
3. Select the `Render` or `Local` environment.
4. Replace `userPassword` locally with the appropriate demo-user password.
5. Run the collection in sequence.

Do not commit a real password after editing an environment in Bruno.

## Post-deployment automation

Configure the deployment system's secret store with the demo-user password, expose it to the job as `CRM_API_TEST_PASSWORD`, install Bruno CLI in the runner, and run:

```bash
cd apitest
bru run --env Render --env-var userPassword="$CRM_API_TEST_PASSWORD"
```

The command exits non-zero when a request or assertion fails, making it suitable as a post-deployment verification step. The test-generated customer is removed during a successful run.

If the run stops before its delete request, a test customer can remain in the deployed database. Such records are easy to identify: their first name starts with `Bruno`, their last name is `PostDeploy`, and their tags include `post-deployment` and `bruno`.
