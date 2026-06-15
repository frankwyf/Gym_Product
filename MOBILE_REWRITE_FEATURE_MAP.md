# Mobile Rewrite Feature Map

This document maps `GymMaster_wx` mini-program capabilities to the new native mobile app in `gym-mobile-app`.

## Core tabs

1. Home
- Mini-program pages: `pages/home/home`, `pages/notices/notices`, `pages/video/video`, `pages/Search-result/Search-result`
- Current mobile status:
  - Implemented native home shell with slides, notices, facilities, coaches, call CTA
  - Video, notices, and search are exposed as dedicated native routes/placeholders for the next pass

2. Reservation
- Mini-program pages: `pages/appointment/appointment`, `pages/all-venues/all-venues`, `pages/venues/venues`, `pages/facilities/facilities`
- Current mobile status:
  - Implemented facility list, venue detail entry, cart draft persistence
  - Reservation matrix/time slot UI is planned next

3. Community
- Mini-program pages: `pages/post-index/post-index`, `pages/post-details/post-details`, `pages/send/send`
- Current mobile status:
  - Implemented post feed, post detail, comment submission, create-post with image upload

4. Courses / Shop
- Mini-program pages: `pages/index/index`, `pages/goods-details/goods-details`
- Current mobile status:
  - Implemented course list, category filter, search, detail navigation placeholder

5. Profile / Me
- Mini-program pages: `pages/mine/mine`, `pages/mine/user/user`, `pages/wallet/wallet`, `pages/order-list/index`, `pages/signin/sign-in`
- Current mobile status:
  - Implemented profile overview, accounts list, wallet screen, orders screen, logout
  - Daily check-in page is routed as placeholder for next integration

## Auth and support flows

- Login: `pages/login/index` -> implemented
- Register: `pages/regist/index` -> placeholder wired
- Forgot password: `pages/findpassword/index` -> placeholder wired
- Addresses: `pages/select-address/index`, `pages/address-add/index` -> placeholder route planned

## Commerce / checkout flows

- Cart: `pages/shop-cart/index`
- Payment order: `pages/to-pay-order/index`
- Current mobile status:
  - Local cart persistence implemented
  - Dedicated cart/checkout native flows can be expanded from orders + reservation cart data

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
