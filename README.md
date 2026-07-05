# Cafe Jasjisjus — Android App

Android frontend for **Aplikasi Digitalisasi Layanan Cafe Jasjisjus**.

Built with **Kotlin**, **Jetpack Compose**, **Voyager** (navigation), **Ktor** (networking), **Socket.IO** (realtime), and **Clean Architecture** (multi-module).

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material3
- **Navigation:** Voyager (TabNavigator + Navigator)
- **Networking:** Ktor + OkHttp
- **Realtime:** Socket.IO
- **Architecture:** Clean Architecture (multi-module)
- **Image Loading:** Coil
- **Serialization:** kotlinx.serialization

## Project Structure

```
cafe-app/
├── app/          # Main application module (UI, screens, navigation)
├── core/         # Core utilities, extensions, shared components
├── domain/       # Business logic, models, interactors, repositories
└── data/         # Data sources, API, DTOs, mappers
```

### Modules

| Module | Responsibility |
|---|---|
| `app` | Compose screens, navigation, ViewModel/ScreenModel, DI |
| `core` | Common utilities, extensions, theme, components |
| `domain` | Use cases (`CancelOrder`, `CreateOrder`, `PayOrder`), domain models, repository interfaces |
| `data` | API definitions (`Cafej3Api`), DTOs, repository implementations, mappers |

## Features

- Menu browsing with category filtering
- Cart management with item note support
- Order creation (guest + authenticated) — includes optional `userOfferId` for instant discount
- Doku payment via WebView
- Realtime order tracking (Socket.IO)
- Order history with cancellation reason display
- Rewards & points (redeem offers, points history)
- Dedicated offer selection screen (grouped by type, shows count)
- Cart price breakdown (subtotal, discount, total after discount)
- QR table scanning
- Guest order tracking with secure token
- Active order status polling (30s interval)
- Profile auto-refresh after editing

## Key Implementation Details

### Navigator Pattern (Mihon-style)

Root `Navigator` is captured *before* `TabNavigator` and provided via `CompositionLocalProvider` inside the tab navigator lambda. This allows tab content screens to push to the root navigator (not the tab navigator's internal one), preventing `ClassCastException`.

### Order Refresh on Return

Uses `DisposableEffect` + `LifecycleEventObserver` + `ON_RESUME` to refresh order data when returning from the payment WebView (since Voyager 1.1.0-beta03 has no `popWithResult`). Same pattern used for profile refresh after edit.

### Offer Selection Communication

Screens communicate selected offer via `OfferSelectionStore` singleton. `OfferSelectionScreen` stores the picked `userOfferId`, and `CartScreenModel.loadUserOffers()` picks it up on resume.

### Offer Applied Before Payment

When creating an order with `userOfferId`, the offer is applied on the backend **before** the Doku payment URL is generated — so the payment amount reflects the discounted price.

### Cart Price Breakdown

New Order tab shows subtotal, discount (calculated client-side as estimate from `discountType`/`discountRate`/`maxDiscount`), and total after discount when an offer is selected.

### Cancellation Reason Display

When `order.status == "CANCELLED"` and `order.cancellationReason` is set, the reason is displayed in red below the status in both `ActiveOrderContent` and `OrderCard`.

### Points History

- `RewardsScreen` shows 3 latest entries with "See all" link
- `PointsHistoryScreen` shows full list (no detail screen)
- Canceled orders with applied offers return the `UserOffer` to `AVAILABLE`

### Date Formatting

`DateUtils.formatDateTime()` converts ISO 8601 strings (e.g. `2026-07-05T04:44:03.051Z`) to `dd MMM yyyy, HH:mm` in device local timezone.

## Setup

```bash
# Open in Android Studio
# Sync Gradle
# Run on emulator or device
```

The app connects to `BASE_URL` from BuildConfig (debug: `http://100.108.202.107:3000` via Tailscale, release: production Railway URL).

## Screens

| Screen | File | Description |
|---|---|---|
| Cart/New Order | `screens/cart/CartOrderScreen.kt` | Cart, order tabs, offer selection, price breakdown, payment |
| CartScreenModel | `screens/cart/CartScreenModel.kt` | Order flow, offer selection, payment initiation |
| OfferSelectionScreen | `screens/cart/OfferSelectionScreen.kt` | Dedicated offer picker with grouping and counts |
| RewardsScreen | `screens/rewards/RewardsScreen.kt` | Balance card, available rewards, 3 latest points |
| PointsHistoryScreen | `screens/rewards/PointsHistoryScreen.kt` | Full points history list |
| OrderHistoryScreen | `screens/orderhistory/OrderHistoryScreen.kt` | Previous orders list with cancellation reason |
| OrderHistoryDetailScreen | `screens/orderhistory/OrderHistoryDetailScreen.kt` | Order detail (reuses ActiveOrderContent) |
| ProfileScreen | `screens/profile/ProfileScreen.kt` | User profile with auto-refresh on resume |
| MenuScreen | `screens/menu/MenuScreen.kt` | Category-filtered menu items |

## Dependencies (key)

| Library | Purpose |
|---|---|
| Voyager | Screen-based navigation, TabNavigator |
| Ktor Client | HTTP networking |
| Socket.IO | Realtime order updates |
| Coil | Image loading |
| kotlinx.serialization | JSON serialization |
| Compose Material3 | UI components |
| DataStore | Local preferences |
