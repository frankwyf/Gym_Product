# Gym Product Mobile

This app is the iOS/Android rewrite of the WeChat mini-program in `GymMaster_wx`.

## Migrated feature map

- Home: slides, notices, facilities, coaches, call/video/location entry points
- Courses: category filter, search, course detail navigation
- Reservation: facilities, venue detail entry, local cart reservation draft
- Community: post feed, post detail, comments, create post with image upload
- Profile: login state, customer info, membership upgrade entry, wallet, orders, logout
- Wallet: account list, create account, top-up, delete
- Orders: unpaid reservation drafts, paid reservations, bills
- Auth: login ready, register/forgot password placeholders wired into navigation

## Stack

- Expo + React Native + TypeScript
- React Navigation (bottom tabs + native stack)
- AsyncStorage for token/cart persistence

## Architecture decision

- Current approach is one shared codebase for both iOS and Android.
- iOS apps can be written in Swift, but React Native still renders native UI controls via platform bridges.
- Split into separate Swift and Kotlin apps only when platform-specific UX/performance requirements clearly exceed the value of shared code.

## Run

```powershell
npm --prefix gym-mobile-app install
npm --prefix gym-mobile-app start
npm --prefix gym-mobile-app run android
npm --prefix gym-mobile-app run ios
npm --prefix gym-mobile-app run verify
```

## Notes

- The backend base URL currently follows the mini-program default and is defined in `src/constants/config.ts`.
- Some screens are already functional against the current REST endpoints; some detail flows remain placeholders where backend response shape still needs confirmation.
- The goal of this iteration is a readable, extensible native app foundation that preserves existing front-end feature boundaries before deep UI parity work.
- Full feature mapping is documented in `../MOBILE_REWRITE_FEATURE_MAP.md`.
