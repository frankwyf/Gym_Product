export function toRememberMe(value) {
  if (value === true || value === 'true') {
    return true
  }
  if (value === false || value === 'false') {
    return false
  }
  return Boolean(value)
}

export function buildLoginFormFromCookies(currentForm, cookieStore, decryptFn) {
  const username = cookieStore.get('username')
  const password = cookieStore.get('password')
  const rememberMe = cookieStore.get('rememberMe')

  return {
    username: username === undefined ? currentForm.username : username,
    password: password === undefined ? currentForm.password : decryptFn(password),
    rememberMe: rememberMe === undefined ? false : toRememberMe(rememberMe),
    code: currentForm.code,
    uuid: currentForm.uuid
  }
}

export function persistLoginCookies(loginForm, cookieStore, encryptFn) {
  if (loginForm.rememberMe) {
    cookieStore.set('username', loginForm.username, { expires: 30 })
    cookieStore.set('password', encryptFn(loginForm.password), { expires: 30 })
    cookieStore.set('rememberMe', true, { expires: 30 })
    return
  }

  cookieStore.remove('username')
  cookieStore.remove('password')
  cookieStore.remove('rememberMe')
}
