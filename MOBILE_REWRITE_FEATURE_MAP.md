# Mobile Rewrite Feature Map

This document maps `GymMaster_wx` mini-program capabilities to the new native mobile app in `gym-mobile-app`.

## Core tabs

1. Home
- Mini-program pages: `pages/home/home`, `pages/notices/notices`, `pages/video/video`, `pages/Search-result/Search-result`
- Current mobile status:
  - Implemented native home shell with slides, notices, facilities, coaches, call CTA
  - Implemented dedicated native pages for video playback, notices list, and search result aggregation

2. Reservation
- Mini-program pages: `pages/appointment/appointment`, `pages/all-venues/all-venues`, `pages/venues/venues`, `pages/facilities/facilities`
- Current mobile status:
  - Implemented facility list -> venue category screen -> venue detail entry, cart draft persistence
  - Added loading/empty/error states and retry actions for reservation list pages
  - Reservation matrix/time slot UI is planned next

3. Community
- Mini-program pages: `pages/post-index/post-index`, `pages/post-details/post-details`, `pages/send/send`
- Current mobile status:
  - Implemented post feed, post detail, comment submission, create-post with image upload
  - Added feed error-state/retry handling and submit-in-progress guards to avoid duplicate actions

4. Courses / Shop
- Mini-program pages: `pages/index/index`, `pages/goods-details/goods-details`
- Current mobile status:
  - Implemented course list, category filter, search, detail navigation placeholder

5. Profile / Me
- Mini-program pages: `pages/mine/mine`, `pages/mine/user/user`, `pages/wallet/wallet`, `pages/order-list/index`, `pages/signin/sign-in`
- Current mobile status:
  - Implemented profile overview, accounts list, wallet screen, orders screen, logout
  - Implemented daily check-in monthly calendar and local persistence

## Auth and support flows

- Login: `pages/login/index` -> implemented
- Register: `pages/regist/index` -> implemented (email captcha + register API)
- Forgot password: `pages/findpassword/index` -> implemented (email validation + reset API)
- Addresses: `pages/select-address/index`, `pages/address-add/index` -> implemented (add/edit/delete/default with local persistence)

## Commerce / checkout flows

- Cart: `pages/shop-cart/index`
- Payment order: `pages/to-pay-order/index`
- Current mobile status:
  - Local cart persistence implemented
  - Implemented account selection + pay flow in Orders screen (mapped to `bill/pay`)
  - Note: payment success/failure branch behavior is implemented by backend response code convention and should be rechecked in real backend integration

## Backend/API observations

- The mini-program directly consumes the Java backend at `http://172.20.10.2:8087/`
- Key route groups already mapped in native code:
  - `loginCus/*`
  - `until/*`
  - `venue/*`
  - `posts/*`
  - `account/*`
  - `customer/*`
  - `reservation/*`
  - `bill/*`

## Current rewrite philosophy

- Preserve the original feature boundaries first
- Build one shared React Native codebase for both iOS and Android
- Keep backend contract close to the mini-program to reduce migration risk
- Replace WeChat-only capabilities with native equivalents:
  - storage -> AsyncStorage
  - image upload -> Expo Image Picker + multipart upload
  - location -> Expo Location
  - navigation -> React Navigation
