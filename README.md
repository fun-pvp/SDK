# FunPvP SDK

**Extensible base to speed up development on FunPvP.**

SDK is a lightweight **Minecraft 1.8 plugin SDK** providing easy access to player, rank, and other APIs for quick plugin development.

---

## 🌟 Core Concept

The SDK is centered around **ApiManagement**.
You can enable or disable APIs, and retrieve them when needed:

```java
// Enable a new API
SDK.get().getApiManagement().enable(new PlayersApi());
SDK.get().getApiManagement().enable(new RankApi());

// Disable an API
SDK.get().getApiManagement().disable(new PlayersApi());

// Get an API instance
PlayersApi playersApi = SDK.get().getApiManagement().get(API.PLAYERS);
RankApi rankApi = SDK.get().getApiManagement().get(API.RANK);
```

This system allows you to **modularly activate only the APIs you need** and keeps our plugin clean and maintainable.

---

## 📦 Default APIs

* `PlayersApi` — Manage players, stats, and statistics.
* `RankApi` — Manage ranks, and permissions.
* `MapApi` — Manage maps.
* `ServerApi` — Manage infrastructure.
* `EconomyApi` — Manage economy.
* `ChatApi` — Manage chat.
* `ScoreboardApi` — Manage scoreboards.
* `WorldApi` — Manage world.
* `ItemApi` — Manage items.
* `EntityApi` — Manage entities.
* *(More APIs can be added as the SDK grows.)*

---

## ⚡ License

MIT License. See `LICENSE` for details.
