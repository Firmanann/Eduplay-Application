```json
{
  "success": true,
  "data": {} 
}

```
---
### 1. Domain: Auth (Autentikasi)
---
**`POST /api/auth/register/user`** (Daftar User Biasa)
* **Auth:** Tidak ada
* **Request Body:**
```json
{
  "name": "Budi Santoso",
  "email": "budi@gmail.com",
  "password": "Password123!"
}
```
* **Response (201 Created):** `data: null`
---
**`POST /api/auth/register/contributor`** (Daftar Kreator)
* **Auth:** Tidak ada
* **Request Body:**
```json
{
  "name": "Guru Hebat",
  "email": "guru@gmail.com",
  "password": "Password123!",
  "portofolio": "link",
  "bankName": "BCA",
  "bankAccount": "87654321"
}
```
* **Response (201 Created):** `data: null`
---
**`POST /api/auth/login`**
* **Auth:** Tidak ada
* **Request Body:**
```json
{
  "email": "budi@gmail.com",
  "password": "Password123!"
}

```
* **Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI...",
  "id": "e2f12a-...",
  "name": "Budi Santoso",
  "email": "budi@gmail.com",
  "role": "USER"
}

```
---
**`POST /api/auth/logout`**
* **Auth:** Bearer Token
* **Request Body:** Tidak ada
* **Response (204 No Content):** Tidak ada *body response*.
---
**`GET /api/auth/me`**
* **Auth:** Bearer Token
* **Request Body:** Tidak ada
* **Response (200 OK):**
```json
{
  "id": "e2f12a-...",
  "name": "Budi Santoso",
  "email": "budi@gmail.com",
  "role": "USER",
  "balance": 0,
  "bankName": null,
  "bankAccount": null
}
```
---
### 2. Domain: Catalog (Marketplace Publik)
---
**`GET /api/catalog`**
* **Auth:** Opsional
* **Query Params (Opsional):** `?subject=Matematika&category=GAME`
* **Response (200 OK):**
```json
[
  {
    "id": "c1f12a-...",
    "title": "Kuis Matematika Dasar",
    "price": 0,
    "thumbnailUrl": "https://link.com/img.jpg",
    "category": "QUIZ",
    "subject": "Matematika",
    "contributorName": "Guru Hebat"
  }
]

```
---
**`GET /api/catalog/{id}`**
* **Auth:** Opsional
* **Response (200 OK):**
```json
{
  "id": "c1f12a-...",
  "title": "Kuis Matematika Dasar",
  "description": "Latihan soal penjumlahan",
  "price": 0,
  "thumbnailUrl": "https://link.com/img.jpg",
  "category": "QUIZ",
  "subject": "Matematika",
  "gradeLevel": "SD",
  "contributorName": "Guru Hebat",
  "createdAt": "2026-06-18T10:00:00Z"
}
```
---
**`GET /api/catalog/taxonomies`** (Data *Dropdown Filter*)
* **Auth:** Tidak ada
* **Response (200 OK):**
```json
{
  "categories": ["GAME", "QUIZ", "INTERACTIVE"],
  "subjects": ["Matematika", "Sejarah", "Sains"],
  "gradeLevels": ["SD", "SMP", "SMA"]
}

```
---
**`POST /api/catalog/{id}/purchase`**
* **Auth:** Bearer Token (User Biasa)
* **Request Body:** Tidak ada
* **Response (201 Created):**
```json
{
  "purchaseId": "p1f12a-...",
  "contentId": "c1f12a-...",
  "pricePaid": 0
}

```
---
### 3. Domain: Library (Koleksi Pemain)
---
**`GET /api/library`**
* **Auth:** Bearer Token
* **Response (200 OK):**
```json
[
  {
    "purchaseId": "p1f12a-...",
    "purchasedAt": "2026-06-18T10:05:00Z",
    "content": {
      "id": "c1f12a-...",
      "title": "Kuis Matematika Dasar",
      "thumbnailUrl": "https://link.com/img.jpg"
    }
  }
]

```
---
**`GET /api/library/{id}/play`** (Akses Play)
* **Auth:** Bearer Token
* **Response (200 OK):**
```json
{
  "fileUrl": "https://storage.com/game.html"
}

```
---
**`GET /api/library/{id}/download`** (Akses Download)
* **Auth:** Bearer Token
* **Response (200 OK):**
```json
{
  "downloadUrl": "https://storage.com/game.zip"
}

```
---
### 4. Domain: Contributor (Dasbor Kreator)
---
**`POST /api/contents`** (Upload Konten)
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Request Body:**
```json
{
  "title": "Kuis Sejarah RI",
  "description": "Kuis interaktif kemerdekaan",
  "price": 15000,
  "fileUrl": "https://storage.com/game.html",
  "thumbnailUrl": "https://storage.com/thumb.jpg",
  "category": "GAME",
  "subject": "Sejarah",
  "gradeLevel": "SMP"
}

```
* **Response (201 Created):** `data: null`
---
**`GET /api/contents/my-contents`** (List Karya Sendiri)
---
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Response (200 OK):**
```json
[
  {
    "id": "c1f12a-...",
    "title": "Kuis Sejarah RI",
    "price": 15000,
    "createdAt": "2026-06-18T11:00:00Z"
  }
]

```
---
**`PUT /api/contents/{id}`** (Edit Konten)
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Request Body:**
```json
{
  "title": "Kuis Sejarah RI (Update)",
  "description": "Revisi soal nomor 5",
  "price": 20000,
  "category": "Kuis",
  "subject": "Sejarah",
  "gradeLevel": "SMA",
  "fileUrl": "https://link-baru.com/file",
  "thumbnailUrl": "https://link-baru.com/gambar"
}
```
* **Response (200 OK):** `data: null`
---
**`DELETE /api/contents/{id}`** (Hapus Konten)
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Response (200 OK):** `data: null`
---
**`GET /api/contributor/balance`**
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Response (200 OK):**
```json
{
  "balance": 45000,
  "bankName": "BCA",
  "bankAccount": "87654321"
}

```
---
**`GET /api/contributor/transactions`** (Riwayat Terjual)
* **Auth:** Bearer Token (Role: CONTRIBUTOR)
* **Response (200 OK):**
```json
[
  {
    "purchaseId": "p2x99z-...",
    "contentTitle": "Kuis Sejarah RI",
    "buyerName": "Budi Santoso",
    "pricePaid": 15000,
    "purchasedAt": "2026-06-18T12:30:00Z"
  }
]
```
---