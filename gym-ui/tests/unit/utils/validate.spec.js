const {
  isExternal,
  validUsername,
  validEmail,
  validLowerCase,
  validUpperCase,
  validAlphabets,
  isArray,
  isString
} = require('@/utils/validate')

describe('utils/validate', () => {
  test('should detect external links', () => {
    expect(isExternal('https://example.com')).toBe(true)
    expect(isExternal('mailto:team@gym.com')).toBe(true)
    expect(isExternal('/internal/path')).toBe(false)
  })

  test('should validate allowed usernames only', () => {
    expect(validUsername('admin')).toBe(true)
    expect(validUsername(' editor ')).toBe(true)
    expect(validUsername('guest')).toBe(false)
  })

  test('should validate email format', () => {
    expect(validEmail('coach@gym.com')).toBe(true)
    expect(validEmail('bad-email')).toBe(false)
  })

  test('should validate alphabet case helpers', () => {
    expect(validLowerCase('abc')).toBe(true)
    expect(validLowerCase('Abc')).toBe(false)
    expect(validUpperCase('ABC')).toBe(true)
    expect(validUpperCase('ABc')).toBe(false)
    expect(validAlphabets('AbCd')).toBe(true)
    expect(validAlphabets('Ab1')).toBe(false)
  })

  test('should validate string and array type helpers', () => {
    expect(isString('hello')).toBe(true)
    expect(isString(123)).toBe(false)
    expect(isArray([])).toBe(true)
    expect(isArray({})).toBe(false)
  })
})
