# Cafe Jasjisjus — Build Plan & Current State

## Architecture

Multi-module clean architecture with Voyager navigation, Koin DI, Coil,
Material3 theming, kotlinx.serialization.

```
cafe-app/
├── core/          Network, preferences, DI module
├── data/          API client, repositories, DTOs, mappers, DI module
├── domain/        Entities, interactors (use-cases), repository interfaces
└── app/           Screens, ScreenModels, components, theme, DomainModule DI
```

### Navigation (Voyager)
- Screens: `data object : Screen` or `data class` with params
- ScreenModels: `class XxxScreenModel(dep: Dep = inject()) : ScreenModel` with
  `rememberScreenModel { XxxScreenModel() }`
- Root navigator captured before TabNavigator (Mihon pattern) to allow
  screen pushes from tab content
- Screen refresh on return via DisposableEffect + LifecycleEventObserver

### Backend
- Dev: `http://100.108.202.107:3000` (Tailscale, localhost)
- Production: `https://backend-cafe-jasjisjus-production.up.railway.app`
- Toggle via single `BASE_URL` constant in `NetworkPreference.kt`

---

## Screens & Status

| Screen | Pull-to-refresh |
|---|---|
| LoginScreen | N/A |
| RegisterScreen | N/A |
| ScanCheckInScreen | N/A |
| MainScreen (tab host) | N/A |
| MenuScreen | Planned |
| CartOrderScreen | N/A |
| OfferSelectionScreen | N/A |
| RewardsScreen | Planned |
| PointsHistoryScreen | Planned |
| ExpandedRewardsScreen | N/A |
| OrderHistoryScreen | Planned |
| OrderHistoryDetailScreen | N/A |
| ProfileScreen | Done (lifecycle auto-refresh) |
| AppSettingsScreen | N/A |
| EditProfileScreen | Done |
| FinancialLedgerScreen | N/A |
| PaymentWebViewScreen | N/A |

---

## Features

### Authentication
- Login with JWT (access + refresh tokens stored in preferences)
- Auto-refresh on 401 via ktor pipeline
- Register new account
- Logout = clear local tokens

### Menu
- Category tabs with horizontal scroll
- Menu items as cards with image, name, category, price
- Image loading with loading spinner (CircularProgressIndicator) and error
  fallback (BrokenImage icon) via SubcomposeAsyncImage
- Sold-out items greyed out with overlay
- Add to cart / increment / decrement per item

### Cart & Ordering
- Cart persisted to SharedPreferences (survives app restart)
- Multiple tabs: "New Order" (cart) and "Orders" (active orders)
- Offer selection via dedicated OfferSelectionScreen with grouping + count badges
- Price breakdown: Subtotal, Discount, Total
- Doku payment via WebView with callback URL interception
- Payment flow: create order (with optional offer) → Doku URL → WebView → paid
- Active order tracking with per-item status chips

### Rewards & Points
- Balance card with gradient background
- Points history (full list in PointsHistoryScreen, latest 3 in RewardsScreen)
- Formula: `floor(totalPrice * 3 / 2000)` earned per order at DONE status
- Offer display (horizontal scroll) + expanded full list
- Redeem flow

### Order History
- List of past orders with cancellation reason (shown in red)
- Detail screen reusing ActiveOrderContent
- Cancel order (only when PENDING + UNPAID)
- Expired orders auto-cancelled by cron job

### Profile
- Real data from `GET /auth/me`
- Auto-refresh on resume (after editing profile)
- Edit profile (name, phone)

### Pull-to-refresh planned
- MenuScreen: refresh menu items + categories
- RewardsScreen: refresh balance, history, available offers
- OrderHistoryScreen: refresh order list

---

## Complete

- Double status bar padding fix
- Dark mode MaterialTheme.colorScheme across all screens
- Loading indicators on all API screens
- Cart persistence (SharedPreferences via PreferenceStore)
- Sold-out handling + auto cart removal
- Tab switching via CompositionLocal (LocalTabSwitcher)
- Tab state persistence via Voyager TabNavigator saveableState()
- AppSettingsScreen (System/Dark/Light theme picker)
- ProfileScreen real API data (GET /auth/me) + auto-refresh
- Coil network image loading (coil-network-okhttp)
- Role removal (CASHIER removed, data object MainScreen)
- Dispatchers.IO on all interactors
- QR Scanner (CameraX + MLKit BarcodeScanning)
- DI modularization (CoreModule, DataModule, DomainModule)
- Menu screen UI (HeroSection, CategoryTabs, MenuItemCard)
- Rewards screen (gradient balance card, offers, latest 3 points)
- Points history screen
- Expanded offers screen
- Login/Register screens
- Cart order screen with New Order + Orders tabs
- Offer selection screen with grouping and count badges
- Doku payment WebView
- Order history list + detail screen
- Payment expiry cron (60 min, runs every 1 min)
- Cancel returns UserOffer to AVAILABLE
- Offer applied before Doku payment creation
- Prisma migrations cleaned (single consolidated)
- Backend modules properly wired (OffersModule exports, OrdersModule imports)
- Image loading indicators (CircularProgressIndicator + BrokenImage fallback)
- Production base URL as default (toggle one constant for dev)
