# Cafe Jasjisjus — API Documentation

**Base URL:** `https://backend-cafe-jasjisjus-production.up.railway.app`

> All `price`, `totalPrice`, `finalPrice` values are returned as **strings** (Decimal). Frontend should parse to `Number`/`double`.

---

## Menu

### GET /menu/items
Retrieve all menu items.

**Response:**
```json
[
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "price": "string",
    "category": "string",
    "imageUrl": "string",
    "isPopular": boolean,
    "isChefSpecial": boolean
  }
]
```

### GET /menu/categories
Retrieve all menu categories.

**Response:**
```json
[
  {
    "id": "string",
    "name": "string"
  }
]
```

---

## Tables

### GET /tables/JASJISJUS-TABLE-01
Get table info & active session.

**Response:**
```json
{
  "id": "string",
  "tableNumber": "string",
  "status": "AVAILABLE | OCCUPIED",
  "sessionId": "string | null",
  "qrCode": "string"
}
```

---

## Orders

### POST /orders
Create a new order.

**Request Body:**
```json
{
  "tableId": "string",
  "items": [
    {
      "menuItemId": "string",
      "quantity": number,
      "notes": "string"
    }
  ],
  "notes": "string"
}
```

**Response:**
```json
{
  "id": "string",
  "orderNumber": "string",
  "tableId": "string",
  "items": [
    {
      "menuItemId": "string",
      "name": "string",
      "quantity": number,
      "price": "string",
      "totalPrice": "string",
      "notes": "string"
    }
  ],
  "subtotal": "string",
  "tax": "string",
  "totalPrice": "string",
  "status": "PENDING | CONFIRMED | PREPARING | READY | COMPLETED | CANCELLED",
  "createdAt": "string (ISO 8601)"
}
```

### GET /orders/pending
Get all pending orders.

**Response:**
```json
[
  {
    "id": "string",
    "orderNumber": "string",
    "tableId": "string",
    "items": [...],
    "subtotal": "string",
    "totalPrice": "string",
    "status": "PENDING",
    "createdAt": "string"
  }
]
```

### GET /orders/:id
Get a single order by ID.

### PATCH /orders/:id/status
Update order status.

**Request Body:**
```json
{
  "status": "CONFIRMED | PREPARING | READY | COMPLETED | CANCELLED"
}
```

---

## Warehouse

### POST /warehouse/sync
Trigger warehouse data sync.

**Response:**
```json
{
  "success": boolean,
  "message": "string",
  "syncedAt": "string"
}
```

---

## Analytics

### GET /analytics/sales
Get sales data.

**Response:**
```json
{
  "totalRevenue": "string",
  "totalOrders": number,
  "period": {
    "start": "string",
    "end": "string"
  }
}
```

### GET /analytics/top-items
Get top-selling menu items.

**Response:**
```json
[
  {
    "menuItemId": "string",
    "name": "string",
    "totalSold": number,
    "revenue": "string"
  }
]
```

### GET /analytics/daily-summary
Get today's summary.

**Response:**
```json
{
  "date": "string",
  "totalOrders": number,
  "totalRevenue": "string",
  "activeTables": number,
  "pendingOrders": number
}
```

---

## Notes

- All monetary values (`price`, `totalPrice`, `finalPrice`, `subtotal`, `tax`, `totalRevenue`) are **strings** (Decimal) — parse client-side.
- Order status flow: `PENDING → CONFIRMED → PREPARING → READY → COMPLETED` (or `CANCELLED` at any point).
- No auth header required for current endpoints (public API).
- Dates are ISO 8601 format.
