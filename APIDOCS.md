POST /auth/register
- Request Body :
  {
  "name": "Budi Santoso",
  "email": "budi.santoso@sekolah.sch.id",
  "password": "GuruHebat2026",
  "role": "USER",
  "country": "ID"
  }
- Response : (201 Created)
    {
      "success": true,
      "data": {
        "id": "9f1c2e6a-4b3d-4a8e-8f2a-1c0a2d3e4f56",
        "name": "Budi Santoso",
        "email": "budi.santoso@sekolah.sch.id",
        "role": "USER",
        "createdAt": "2026-06-15T08:00:00Z"
      }
    }
---
POST /auth/login
- Request :
  {
  "email": "budi.santoso@sekolah.sch.id",
  "password": "GuruHebat2026"
  }
- Response : (200 OK)
  {
  "success": true,
  "data": {
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "8f3a1b2c-...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
  "id": "9f1c2e6a-4b3d-4a8e-8f2a-1c0a2d3e4f56",
  "name": "Budi Santoso",
  "role": "USER"
  }
  }
  }
---
POST /auth/logout
- Mencabut refresh token yang aktif 
- Header: `Authorization: Bearer <token>`
- Request: Tidak ada body
- Response (204 No Content)
---
GET /auth/me
Header: `Authorization: Bearer <token>`
Response (200 OK):
{
"success": true,
"data": {
"id": "9f1c2e6a-4b3d-4a8e-8f2a-1c0a2d3e4f56",
"name": "Budi Santoso",
"email": "budi.santoso@sekolah.sch.id",
"role": "USER",
"country": "ID",
"currency": "IDR",
"createdAt": "2026-06-15T08:00:00Z"
}
}
---