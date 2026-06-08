jest.mock('@/utils/request', () => jest.fn((config) => Promise.resolve(config)))

const request = require('@/utils/request')
const { login, register, getInfo, logout, getCodeImg } = require('@/api/login')

describe('login api', () => {
  beforeEach(() => {
    request.mockClear()
  })

  test('login should call /login with token disabled header', async () => {
    await login('admin', '123456', 'abcd', 'uuid-1')
    expect(request).toHaveBeenCalledWith({
      url: '/login',
      headers: { isToken: false },
      method: 'post',
      data: {
        username: 'admin',
        password: '123456',
        code: 'abcd',
        uuid: 'uuid-1'
      }
    })
  })

  test('register should call /register with token disabled header', async () => {
    const payload = { username: 'new-user', password: 'pwd' }
    await register(payload)
    expect(request).toHaveBeenCalledWith({
      url: '/register',
      headers: { isToken: false },
      method: 'post',
      data: payload
    })
  })

  test('getInfo should call /getInfo', async () => {
    await getInfo()
    expect(request).toHaveBeenCalledWith({
      url: '/getInfo',
      method: 'get'
    })
  })

  test('logout should call /logout', async () => {
    await logout()
    expect(request).toHaveBeenCalledWith({
      url: '/logout',
      method: 'post'
    })
  })

  test('getCodeImg should call /captchaImage with timeout', async () => {
    await getCodeImg()
    expect(request).toHaveBeenCalledWith({
      url: '/captchaImage',
      headers: { isToken: false },
      method: 'get',
      timeout: 20000
    })
  })
})
