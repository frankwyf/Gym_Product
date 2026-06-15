# Gym Product Optimization and Open-Source Plan

## Update (2026-06-15)

### Completed in this iteration
- Added repository-wide validation orchestrator:
  - `scripts/verify-all.ps1`
- Added dedicated validation scripts:
  - `scripts/verify-mobile.ps1` (TypeScript + Expo dependency compatibility)
  - `scripts/verify-miniapp.ps1` (JS syntax + JSON parse)
- Improved docs and runnable commands:
  - Root README now includes mobile/miniapp verification entries.
  - Mobile README now includes `npm run verify`.
- Added mobile npm quality scripts:
  - `typecheck`, `check:deps`, `verify`
- Validation result snapshot:
  - PASS: mobile verify
  - PASS: frontend tests + smoke e2e (smoke skipped when browser/runtime unavailable)
  - PASS: mini-program syntax/config parse
  - WARN: backend compile blocked by Maven environment

### Pending actions requiring user approval (large local installs)
- Install Maven (system-wide) to enable backend compile/start checks end-to-end.
- If later needed for native runtime packaging/debug:
  - Android Studio + SDK
  - Xcode (macOS)

## 1) Current Status

### Repository layout
- Projects are moved to repository root:
  - gym-management-system-master
  - gym-ui
  - gymMaster
  - GymMaster_wx

### Run verification
- gym-ui:
  - Dependency install works with `npm install --legacy-peer-deps --no-audit --no-fund`.
  - Build works with legacy OpenSSL provider on Node 24:
    - `set NODE_OPTIONS=--openssl-legacy-provider` (cmd)
    - `npm --prefix gym-ui run build:prod`
- gym-management-system-master:
  - Cannot build/start yet because `mvn` is not available in PATH.
- gymMaster:
  - Maven wrapper is incomplete (`.mvn/wrapper` missing), and global Maven is also missing.
- GymMaster_wx:
  - Source-level review completed; runtime not executed here (requires WeChat DevTools).

## 2) Functional Domain Map

### gym-management-system-master (Spring Boot + RuoYi style)
- Main domains:
  - Member, VIP, cabinet, assignment, commodity
  - Coach, customer, employee, manager
  - Notice, posts, logs, reservation, bill, venue

### gymMaster (Spring Boot standalone backend)
- Main domains:
  - Account/auth (JWT + Spring Security)
  - Coach, customer, employee, course, reservation
  - Bill, goals, facility, notice, posts/comments, venue
- There is substantial overlap with `gym-management-system-master/gymmaster` domain.

### gym-ui (Vue2 + Element UI)
- Main page groups:
  - `src/views/gym` (member/vip/usage/cabinet/studentAssignment)
  - `src/views/system`, `src/views/monitor`, `src/views/tool`
- API layers:
  - `src/api/gym`, `src/api/system`, `src/api/monitor`, `src/api/tool`

### GymMaster_wx (WeChat mini-program)
- Main pages:
  - appointment, coaches, venues, facilities, notices, wallet, posts
- Shared util:
  - `utils/util.js`, `utils/pay.js`, `utils/city.js`
- Current server URL is hardcoded in `app.js`.

## 3) Key Open-Source Risks

- Missing toolchain reproducibility:
  - No working Maven wrapper for `gymMaster`.
  - Node version mismatch with old Vue2 stack (Node 24 requires compatibility env var).
- Hardcoded/localized config:
  - `D:/ruoyi/uploadPath` in `gym-management-system-master` config.
  - Hardcoded mini-program backend URL in `GymMaster_wx/app.js`.
- Dependency age/security:
  - Legacy Vue2 and older plugin stack.
  - Historical libraries with deprecation warnings.
- Documentation gaps:
  - No single source of truth for architecture and which backend is canonical.

## 4) Optimization Roadmap (Suggested)

### Phase A: Baseline stabilization (1-2 days)
1. Decide canonical backend:
   - Keep one backend as primary, mark the other as legacy/experimental.
2. Reproducible setup:
   - Add Maven Wrapper properly for chosen backend.
   - Pin Node version (recommend 16 LTS for this codebase) via `.nvmrc` and docs.
3. Env templating:
   - Add `.env.example` and application config templates for DB/Redis/JWT/mail.

### Phase B: Test foundation (2-4 days)
1. Backend tests (JUnit5 + SpringBootTest + Testcontainers or H2 profile):
   - Priority services:
     - Reservation conflict/capacity logic
     - Bill creation and settlement
     - Auth/login/token expiry behavior
2. Frontend tests (Jest + Vue Test Utils):
   - API wrapper behavior (`src/utils/request.js`)
   - Critical forms in member/reservation flows
3. E2E tests (Playwright/Cypress):
   - Admin login -> member management -> reservation -> bill workflow

### Phase C: Open-source hardening (2-3 days)
1. Security and compliance:
   - Secret scan, remove historical credentials, enforce env-only secrets.
2. CI pipeline:
   - Build + test matrix for backend/frontend.
3. Docs:
   - Root README with quick-start per project.
   - CONTRIBUTING + LICENSE consistency + architecture overview.

## 5) Immediate Next Actions

1. Install Maven (or provide it in PATH) and verify both backend builds.
2. Add canonical project-level run script docs for Windows/macOS/Linux.
3. Create first automated tests for reservation and bill modules.
4. Add CI workflow to run backend unit tests and frontend build on push.
