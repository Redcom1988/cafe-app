# Cafe Jasjisjus — Jetpack Compose Build Plan

Source: UI mockup set (`Frame_1.png`) — 11 screens covering a customer-facing
cafe ordering app, a customer rewards program, and a staff/cashier
"financial ledger" mode. This plan turns those mocks into a buildable
Compose app: navigation graph, screen inventory, component breakdown, and
styling rules.

---

## 1. App Structure & Personas

The mockups actually describe **two apps** (or two modes) sharing one
design system:

1. **Customer app** — Sign in → Scan QR to check in → Browse menu → Cart →
   Order → Rewards → Profile.
2. **Cashier / staff app** — Same visual language, different bottom nav
   (Menu / Customer Order / **Financial** / Profile) and an added
   **Financial Ledger** screen for tracking daily income/expenses.

Recommend building as a **single Compose codebase** with a `UserRole`
(`CUSTOMER` / `CASHIER`) that swaps the bottom navigation items and adds the
Financial screen — the Menu, Cart/Order, Rewards, and Profile screens are
visually identical (just re-skinned avatar/name) between the two.

```
com.cafejasjisjus
├── ui/
│   ├── theme/            (Color.kt, Theme.kt, Type.kt — see deliverables)
│   ├── components/       (shared composables, see §4)
│   ├── navigation/        (NavGraph.kt, Destinations.kt)
│   └── screens/
│       ├── auth/          SignInScreen.kt
│       ├── checkin/       ScanCheckInScreen.kt
│       ├── menu/          MenuScreen.kt, MenuSearchScreen.kt
│       ├── cart/          CartOrderScreen.kt
│       ├── rewards/       RewardsScreen.kt
│       ├── profile/       ProfileScreen.kt
│       └── financial/     FinancialLedgerScreen.kt
├── data/                  (models, repositories — out of scope here)
└── MainActivity.kt
```

---

## 2. Navigation Graph

```kotlin
sealed class Destinations(val route: String) {
    object SignIn        : Destinations("sign_in")
    object ScanCheckIn   : Destinations("scan_check_in")
    object Menu          : Destinations("menu")
    object MenuSearch    : Destinations("menu_search")
    object CartOrder     : Destinations("cart_order")
    object Rewards       : Destinations("rewards")
    object Profile       : Destinations("profile")
    object Financial     : Destinations("financial")   // cashier-only
}
```

- **Unauthenticated graph**: `SignIn → ScanCheckIn → Menu`
- **Authenticated graph (bottom-nav scaffold)**: `Menu | CartOrder | Rewards
  | Profile` (customer) or `Menu | CartOrder | Financial | Profile`
  (cashier), hosted inside a single `Scaffold` with persistent
  `NavigationBar`.
- `MenuSearch` is a secondary destination/overlay reached from the search
  icon in the top bar (modeled as the grid-style menu+filter chips screen).

---

## 3. Screen Inventory

For each screen: purpose, key elements top-to-bottom, and unique
components needed.

### 3.1 Sign In (`SignInScreen`)
- **Purpose**: phone/email auth entry point, app's first impression.
- **Elements**:
  - Hero photo banner (coffee + pastry), top ~30% of screen, cream
    background below it.
  - Headline "Welcome back to Cafe Jasjisjus" (brand-brown, bold, 2 lines)
    + subtext "Sign in to earn rewards and view your favorites."
  - Segmented control: **Phone | Email** toggle (pill-shaped, active tab
    white pill on blush-pink track).
  - Label "Phone Number" + input field with `+1` prefix chip and
    placeholder `(555) 000-0000` (rounded rect, taupe fill, no border).
  - Primary CTA button **Sign In** (full width, pill, brand brown,
    white bold text).
  - Footer: "New to the cafe? **Create an account**" (link in accent
    color) + small "Help Center · Privacy Policy" links, centered, muted.
- **Components needed**: `HeroImageHeader`, `SegmentedToggle`,
  `LabeledTextField`, `PrimaryButton` (pill), `TextLink`.
- **State**: `phoneOrEmail: AuthMode`, `inputValue: String`, loading/error.

### 3.2 Scan to Check-In (`ScanCheckInScreen`)
- **Purpose**: QR scan to seat/start a table session.
- **Elements**:
  - Title "Scan to Check-in" + instruction text.
  - Camera viewfinder card: rounded square, dark photo background, white
    corner-bracket frame overlay (camera reticle), small icon badge
    top-right of the frame.
  - Secondary button **Manual Entry** (outline/ghost pill button).
  - "Having trouble scanning?" text link, centered.
  - "Pro Tip" info card (light card, info icon, "Hold your phone about 6
    inches from the code.").
- **Components needed**: `CameraViewfinderFrame` (custom Canvas/SVG corner
  brackets over a `CameraPreview` or static fallback image),
  `SecondaryButton` (outline pill), `InfoTipCard`.

### 3.3 Menu — Browse / Vertical Feed (`MenuScreen`)
This is the most complex screen; it repeats across both customer and
cashier apps with only the top bar avatar differing.
- **Elements top to bottom**:
  - Top app bar: cutlery logo + app name (left), search icon + avatar
    (right), cream background, no elevation/divider.
  - Hero promo banner: full-bleed teal/orange illustrated card ("HERO" +
    mascot graphic), overlaid text "SIGNATURE EXPERIENCE" (eyebrow,
    letter-spaced) + "Crafted with Passion" (large bold white headline) +
    faint watermark text.
  - Category pill row: **Menu | Appetizers | Main Course | …**, horizontal
    scroll, active pill = solid brown, inactive = cream/outline.
  - Vertical list of **MenuItemCard**s:
    - Large rounded photo (top), floating price badge top-right
      ("Rp.xxx", white pill with brown text), optional "POPULAR" badge
      top-left (dark pill, white text) on first card.
    - Below photo: item name (bold), short descriptor (muted, small caps
      style placeholder), full-width **Add to Cart** button (brown pill,
      cart icon + label).
  - One **Chef's Special** card variant: dark photo top half, solid
    burnt-orange bottom half containing a small star badge + "CHEF'S
    SPECIAL" label, item name, descriptor, price (left) + **Quick Add**
    pill button (white, right-aligned) — note this card has NO separate
    photo/info split, info is overlaid on a color block, not white.
  - Bottom navigation bar (persistent across authenticated screens):
    4 items — customer: `Menu | My Order | Rewards | Profile`; cashier:
    `Menu | Customer Order | Financial | Profile`. Active item = filled
    brown rounded pill behind icon+label; inactive = muted gray icon/label
    on transparent.
- **Components needed**: `CafeTopBar`, `PromoHeroBanner`, `CategoryPillRow`,
  `MenuItemCard` (standard variant), `ChefSpecialCard` (overlay variant),
  `PriceBadge`, `PopularBadge`, `BottomNavBar`.

### 3.4 Menu — Search / Grid (`MenuSearchScreen`)
A second menu layout: search-first, 2-column grid, paired with an
in-progress cart drawer.
- **Elements**:
  - Search bar (rounded rect, search icon + placeholder "Search menu
    items, ingredients, or codes...").
  - Filter pill row: **All | Main | Drink | …** (same pill style as
    category row, "All" active).
  - **2-column grid** of compact `MenuGridCard`s: square-ish photo,
    floating price badge, item name + category label below, no button
    (tap to add) — used for quick browse/cashier order entry.
  - Trailing "add custom item" tile (dashed border, **+** icon) and a
    "more" tile (**•••**) at the end of the grid — cashier-specific
    quick actions.
  - **Active order summary panel** (bottom sheet / card, only present
    when items are added): table number + "Dine In" tag, line items
    (photo thumbnail, qty stepper `– n +`, price, delete icon), an "Order
    Notes" field with placeholder text already filled in, Subtotal / Tax
    (8.5%) / **Total** rows, then two buttons side by side: **Split**
    (outline) and **Order** (filled brown) — full width together.
- **Components needed**: `SearchBar`, `FilterPillRow`, `MenuGridCard`,
  `AddCustomItemTile`, `OrderSummarySheet`, `QtyStepper`, `OrderNoteField`,
  `PriceSummaryRow`, dual-button footer (`OutlinedButton` + `PrimaryButton`).
- **Note**: This screen is essentially `MenuScreen` content type +
  `CartOrderScreen` panel combined; consider building `OrderSummarySheet`
  as a standalone reusable composable shown from both Menu and dedicated
  Cart screens.

### 3.5 Rewards (`RewardsScreen`)
- **Elements**:
  - **Balance card** (hero card, solid brown background, rounded 20dp):
    "Current Balance" label (cream, small) → "1,250 **Points**" (large
    bold white number + smaller "Points" suffix), medal/ribbon icon
    top-right, "Gold Member Status · 250 to next reward" caption, thin
    horizontal progress bar (white fill on translucent track) at bottom.
  - "Available Rewards" section header + "See all" link (accent color,
    right-aligned).
  - Horizontal-scrolling **RewardCard**s: photo top (rounded), "X Points"
    (accent text), reward title (bold), **Redeem** button (muted
    taupe/gray pill, full width).
  - "Top 4 Users" leaderboard section header.
  - **LeaderboardRow** list inside a white card: rank number (left,
    muted), avatar photo (circular), name (bold) + membership tier
    (muted caption), points value (right-aligned, accent color, bold,
    "pts" suffix smaller) — divided by hairline dividers.
  - Bottom nav (Rewards tab active).
- **Components needed**: `PointsBalanceCard` (with `LinearProgressIndicator`
  styled), `SectionHeaderWithAction`, `RewardCard`, `LeaderboardRow`.

### 3.6 Profile (`ProfileScreen`)
- **Elements**:
  - White card: circular avatar (large, ~90dp) with small brown
    "verified" badge bottom-right overlapping the avatar, name (bold,
    centered), "Member since {date}" caption (muted, centered).
  - Stack of 4 **SettingsRow**s (each its own white rounded card): leading
    icon in a soft circular chip (tan/cream bg), title (bold) + subtitle
    (muted caption), trailing chevron `>`. Rows: Edit Profile, Order
    History, App Settings, Help Center.
  - **Log Out** button at the bottom: full-width pill, solid dark brown,
    white text + leave/exit icon.
- **Components needed**: `ProfileHeaderCard`, `SettingsRow` (reusable,
  icon+title+subtitle+chevron), `DestructiveButton` or reuse
  `PrimaryButton` with brown-dark variant.
- Cashier variant: same layout, "Alex Cashier · Cashier since October
  2022" — fully reuse the component, just swap copy/role string.

### 3.7 Financial Ledger (`FinancialLedgerScreen`) — cashier only
- **Elements**:
  - Page header: "Financial Ledger" (bold) + "Keuangan • Tracking daily
    artisan operations" subtitle (muted, mixed ID/EN — keep as-is per
    source).
  - Row: date selector chip (calendar icon + "Oct 12, 2023") + primary
    **+ Add Transaction** button (brown pill, right-aligned).
  - 3 stacked **StatCard**s (white rounded cards, full width): label
    (small caps, muted) + circular icon chip top-right (tinted per
    category: bank/income icon peach, wallet icon peach, cart icon red-
    tinted) → big bold value ("Rp. 12.842.500") → small caption below
    (e.g. "+12.5% from last month" in green, or descriptive muted text).
    Cards: **Total Balance**, **Monthly Income**, **Active Expenses**.
  - Tab row: **All Transactions** (active, pill) | Income | Expenses, plus
    trailing filter icon + download icon (icon buttons, muted, far right).
  - List of **TransactionRow** cards (white rounded cards): leading
    circular icon chip (category-specific icon/tint), title (bold,
    2-line wrap allowed e.g. "Artisan Sourdough Wholesale"), date/time
    + status tag caption (e.g. "OCT 12, 10:45 AM · INCOME"), trailing
    amount (bold, green `+Rp` for income / red `-Rp` for expense) +
    status caption under it ("Completed" / "Processing" / "Settled").
  - "VIEW FULL ARCHIVE" centered text link (underlined, muted/accent,
    small caps) at the bottom of the list.
  - Bottom nav (Financial tab active, cashier nav set).
- **Components needed**: `DateChip`, `StatCard` (icon + label + value +
  caption), `TransactionFilterTabs`, `TransactionRow`, `AmountText`
  (auto-colors based on sign/type), `TextLinkCentered`.

### 3.8 Cart / My Order (`CartOrderScreen`)
Distinct full-screen version of the order summary panel seen embedded in
§3.4 — used as the dedicated "My Order"/"Customer Order" bottom-nav tab.
- **Elements**: identical structure to the `OrderSummarySheet` in §3.4 but
  full-page: table/dine-in tag header, line items with qty steppers and
  delete, order notes field, subtotal/tax/total breakdown, **Split** +
  **Order** button row.
- Reuses `OrderSummarySheet` content composable from §3.4 inside a full
  `Scaffold` page instead of a bottom sheet.

---

## 4. Shared Component Library (`ui/components/`)

| Component | Used in | Notes |
|---|---|---|
| `CafeTopBar` | All authenticated screens | Logo+title left, action icons right, transparent/cream, no shadow |
| `BottomNavBar` | All authenticated screens | 4 items, active = filled pill (brown) behind icon+label, role-aware item set |
| `PrimaryButton` | Everywhere | Full-width pill, brown fill, white bold label, optional leading icon |
| `SecondaryButton` / `OutlinedPillButton` | Manual Entry, Split | Pill, transparent fill, brown/taupe border+text |
| `SegmentedToggle` | Sign In (Phone/Email) | 2-option pill switch |
| `LabeledTextField` | Sign In, search bars | Rounded rect, no visible border, taupe/cream fill |
| `MenuItemCard` | Menu screen | Photo + price badge + name + desc + CTA |
| `ChefSpecialCard` | Menu screen | Color-block overlay variant of MenuItemCard |
| `MenuGridCard` | Search/grid menu | Compact 2-col variant |
| `CategoryPillRow` / `FilterPillRow` | Menu screens | Horizontal scroll chip row |
| `PriceBadge`, `StatusBadge` (Popular/Dine In) | Cards | Floating pill badges over images |
| `QtyStepper` | Cart/order rows | `–  n  +` control |
| `OrderSummarySheet` | Cart, Menu-grid | Reusable order recap block |
| `PointsBalanceCard` | Rewards | Hero stat card w/ progress bar |
| `RewardCard` | Rewards | Horizontal scroll reward tile |
| `LeaderboardRow` | Rewards | Rank/avatar/name/points row |
| `SettingsRow` | Profile | Icon chip + title/subtitle + chevron |
| `StatCard` | Financial | Icon chip + label + big value + caption |
| `TransactionRow` | Financial | Icon chip + title/meta + amount/status |
| `SectionHeaderWithAction` | Rewards, Financial | Title left, "See all" link right |
| `InfoTipCard` | Check-in | Icon + tip text, light card |
| `CameraViewfinderFrame` | Check-in | Custom-drawn corner brackets |

---

## 5. Styling System

### 5.1 Color usage rules (see `Color.kt` for hex values)
- **Background**: `CreamBackground` for all screen scaffolds — nothing in
  this app sits on pure white at the page level.
- **Cards/surfaces**: `SurfaceWhite` for elevated content (menu item
  cards, list rows, settings rows, stat cards) — gives the cream-on-white
  layering seen throughout.
- **Primary actions**: `CoffeeBrown` fill + white text, always pill-shaped
  (`PillShape`), used for Sign In, Add to Cart, Order, Log Out, Add
  Transaction, active category pill, active bottom-nav pill.
- **Accent text/links**: `TerracottaAccent` for "Create an account", "See
  all" links, leaderboard point values, price text inside grid cards.
- **Secondary/muted buttons**: `MutedTaupe` fill for "Redeem" (signals
  "available but not primary CTA").
- **Status colors**: `IncomeGreen` / `ExpenseRed` exclusively in the
  Financial Ledger screen for amount text and category tags; never reuse
  these for non-financial UI to keep them meaningful.
- **Dark overlays on photography**: badges like "POPULAR" sit on
  `BadgeDarkBg` (near-black, ~85% opacity) directly atop images for
  contrast; price badges instead use light `PriceBadgeBg` chips.

### 5.2 Shape system
- **Pills** (`PillShape`, full 50% corner): all primary/secondary buttons,
  segmented toggles, category/filter chips, bottom-nav active indicator,
  status badges (Popular, Dine In, Income/Expense tags).
- **Large rounded rect** (`CafeShapes.large`, 20dp): page-level cards
  (balance card, stat cards, settings card, order summary sheet).
- **Medium rounded rect** (`CafeShapes.medium`, 16dp): menu item card
  containers, photo corners, input fields.
- **Small rounded rect** (`CafeShapes.small`, 12dp): nested elements like
  thumbnail images inside transaction rows.
- Avoid sharp corners anywhere — the design has zero 0dp-radius elements.

### 5.3 Typography
- Headlines (`headlineMedium`/`headlineSmall`) are bold, slightly tight
  line-height — used sparingly: sign-in welcome message, hero banner
  copy, "Financial Ledger" title.
- Body/title text otherwise restrained — most "content" is just bold
  16sp item names + 12–14sp muted descriptors, not a heavy type scale.
- Use `labelSmall` (10sp, bold, letter-spaced) for all-caps eyebrow/badge
  text: "SIGNATURE EXPERIENCE", "GLUTEN FREE", "POPULAR", "INCOME",
  "EXPENSE" — add `letterSpacing = 0.5.sp` where used.
- Numbers (points, prices, balances) should be bold and visually heavier
  than their labels — consider a numeric `fontFeatureSettings = "tnum"`
  for aligned tabular figures in the Financial screen.

### 5.4 Spacing & layout
- Screen horizontal padding: 20dp standard.
- Card internal padding: 16dp.
- Vertical rhythm between cards/sections: 12–16dp; between major sections
  (e.g. balance card → "Available Rewards" header): 24dp.
- Photo aspect ratio in menu cards: roughly 4:3 to 1:1, full-bleed width
  inside the card with rounded top corners matching the card's corner
  radius (no inset/margin around the image).
- Bottom nav height: ~64dp, content scaffolds need bottom padding/inset to
  avoid being obscured (`Scaffold(bottomBar = ...)` handles this natively).

### 5.5 Iconography
- Outline-style icons throughout (cart, search, profile, settings gear,
  chevron, bank, wallet/income, shopping-cart for expenses, calendar,
  download, filter/sort). Recommend `androidx.compose.material.icons
  .outlined.*` or a matched icon pack (e.g. Phosphor/Lucide) for the
  rounded-stroke look seen in the mocks rather than Material's filled
  defaults.
- Icon chips (circular backgrounds behind icons in Profile/Financial rows)
  use `IconChipBg` fill with the icon tinted to match its semantic color
  (brown for neutral, green for income, red/peach for expense-leaning).

### 5.6 Elevation & shadows
- Very soft, low-opacity shadows only (`ShadowColor`, ~10% black-brown) —
  cards read as "lifted" off the cream background mostly through the
  white-vs-cream contrast rather than heavy drop shadows. Use
  `Modifier.shadow(2.dp, shape, ambientColor = ShadowColor, spotColor =
  ShadowColor)` sparingly, primarily on the bottom nav bar and any
  floating sheets.

---

## 6. Build Order (suggested milestones)

1. **Foundation**: `Color.kt`, `Theme.kt`, `Type.kt`, `Shapes` (delivered),
   plus base `PrimaryButton`/`SecondaryButton`/`CafeTopBar`/`BottomNavBar`.
2. **Auth flow**: Sign In → Scan Check-in (low data dependency, good for
   validating theme/components end-to-end).
3. **Menu (core loop)**: `MenuScreen` vertical feed + `MenuItemCard` +
   `ChefSpecialCard` + `CategoryPillRow` — this is the highest-value,
   highest-reuse screen.
4. **Cart/Order**: `OrderSummarySheet` + `QtyStepper`, wire into both the
   grid-menu embedded panel and the standalone Cart screen.
5. **Rewards**: `PointsBalanceCard`, `RewardCard`, `LeaderboardRow`.
6. **Profile**: `ProfileHeaderCard` + `SettingsRow` (shared between
   customer/cashier with a `role` parameter).
7. **Financial Ledger** (cashier-only, most data-dense): `StatCard`,
   `TransactionRow`, filter tabs.
8. **Polish pass**: empty/loading/error states, dark theme check, role
   switching (customer vs cashier nav + Financial visibility).

---

## 7. Open Questions / Assumptions

- Treated "XXXX", "XXXXX" placeholder text in the mock as literal CMS
  placeholders — real copy/content model (item name, description, price,
  currency formatting `Rp.` vs `Rp` with thousands separators) needs
  confirmation.
- Assumed cashier and customer apps share one codebase with a role flag;
  if they're actually meant to ship as fully separate apps, split
  `ui/screens` into `customer/` and `cashier/` modules instead, both
  depending on a shared `:design-system` module containing everything in
  §4 and the theme files.
- No dark-mode mock exists; `Theme.kt`'s dark scheme is an inferred
  inversion, not a pixel-accurate target.
