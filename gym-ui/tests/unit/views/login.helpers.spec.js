const {
  toRememberMe,
  buildLoginFormFromCookies,
  persistLoginCookies
} = require('@/views/login.helpers')

describe('views/login.helpers', () => {
  test('toRememberMe should parse string values correctly', () => {
    expect(toRememberMe('true')).toBe(true)
    expect(toRememberMe('false')).toBe(false)
    expect(toRememberMe(true)).toBe(true)
    expect(toRememberMe(false)).toBe(false)
  })

  test('buildLoginFormFromCookies should merge cookie values and keep code/uuid', () => {
    const currentForm = {
      username: 'default-user',
      password: 'default-pass',
      rememberMe: false,
      code: '8888',
      uuid: 'u-1'
    }

    const cookieStore = {
      get: jest.fn((key) => {
        const mapping = {
          username: 'admin',
          password: 'encrypted-password',
          rememberMe: 'false'
        }
        return mapping[key]
      })
    }

    const next = buildLoginFormFromCookies(currentForm, cookieStore, (raw) => `dec:${raw}`)

    expect(next).toEqual({
      username: 'admin',
      password: 'dec:encrypted-password',
      rememberMe: false,
      code: '8888',
      uuid: 'u-1'
    })
  })

  test('persistLoginCookies should save cookies when rememberMe is true', () => {
    const cookieStore = {
      set: jest.fn(),
      remove: jest.fn()
    }

    persistLoginCookies(
      { username: 'admin', password: '123456', rememberMe: true },
      cookieStore,
      (raw) => `enc:${raw}`
    )

    expect(cookieStore.set).toHaveBeenCalledTimes(3)
    expect(cookieStore.set).toHaveBeenCalledWith('username', 'admin', { expires: 30 })
    expect(cookieStore.set).toHaveBeenCalledWith('password', 'enc:123456', { expires: 30 })
    expect(cookieStore.set).toHaveBeenCalledWith('rememberMe', true, { expires: 30 })
    expect(cookieStore.remove).not.toHaveBeenCalled()
  })

  test('persistLoginCookies should clear cookies when rememberMe is false', () => {
    const cookieStore = {
      set: jest.fn(),
      remove: jest.fn()
    }

    persistLoginCookies(
      { username: 'admin', password: '123456', rememberMe: false },
      cookieStore,
      (raw) => `enc:${raw}`
    )

    expect(cookieStore.remove).toHaveBeenCalledTimes(3)
    expect(cookieStore.remove).toHaveBeenCalledWith('username')
    expect(cookieStore.remove).toHaveBeenCalledWith('password')
    expect(cookieStore.remove).toHaveBeenCalledWith('rememberMe')
    expect(cookieStore.set).not.toHaveBeenCalled()
  })
})
