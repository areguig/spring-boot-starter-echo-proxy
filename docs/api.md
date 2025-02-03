---
layout: default
title: API Reference
nav_order: 3
---

# API Reference
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Management API

The library exposes REST endpoints to manage proxy configurations:

### List Configurations

```http
GET /echo-proxy/configs
```

Returns all current proxy configurations.

### Create Configuration

```http
POST /echo-proxy/configs
Content-Type: application/json

{
  "urlPattern": "/api/test",
  "mode": "MOCK",
  "method": "GET",
  "responseBody": "{\"message\":\"test\"}",
  "statusCode": 200,
  "active": true
}
```

Creates a new proxy configuration.

### Update Configuration

```http
PUT /echo-proxy/configs/{id}
Content-Type: application/json

{
  "urlPattern": "/api/test",
  "mode": "MOCK",
  "method": "GET",
  "responseBody": "{\"message\":\"updated\"}",
  "statusCode": 200,
  "active": true
}
```

Updates an existing configuration.

### Delete Configuration

```http
DELETE /echo-proxy/configs/{id}
```

Deletes a configuration by ID.

## Models

### ProxyConfig

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Unique identifier |
| `urlPattern` | String | URL pattern to match |
| `mode` | ProxyMode | `MOCK` or `PROXY` |
| `method` | HttpMethod | HTTP method to match |
| `responseBody` | String | Response body for mock mode |
| `statusCode` | Integer | Response status code |
| `targetUrl` | String | Target URL for proxy mode |
| `active` | boolean | Whether the config is active |
| `responseHeaders` | Map<String, String> | Custom response headers | 