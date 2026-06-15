# Gym Product Mobile

This app is the iOS/Android rewrite of the WeChat mini-program in `GymMaster_wx`.

## Migrated feature map

- Home: slides, notices, facilities, coaches, call/video/location entry points
- Courses: category filter, search, course detail navigation
- Reservation: facilities, venue detail entry, local cart reservation draft
- Community: post feed, post detail, comments, create post with image upload
- Profile: login state, customer info, membership upgrade entry, wallet, orders, daily check-in, addresses, logout
- Wallet: account list, create account, top-up, delete
- Orders: unpaid reservation drafts, account selection + pay action, paid reservations, bills
- Auth: login, register, forgot-password (captcha + backend flow)
- Home extensions: notices list, search results, video playback
- Reliability polish: loading/empty/error states with retry actions on key list pages
- Data safety polish: invalid-id guards for post/facility/venue navigation and actions

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
- Core screens are functional against current REST endpoints; remaining enhancements are mostly UX parity and richer reservation matrix behavior.
- Reservation flow now follows mini-program semantics more closely: facility list -> venue categories -> venue detail.
- Payment flow is migrated from mini-program `to-pay-order`, but final backend response branching should be confirmed in an integrated runtime.
- The goal of this iteration is a readable, extensible native app foundation that preserves existing front-end feature boundaries before deep UI parity work.
- Full feature mapping is documented in `../MOBILE_REWRITE_FEATURE_MAP.md`.
