import { API_BASE_URL } from '../constants/config'

export async function apiRequest<T>(path: string, options: RequestInit = {}, token?: string | null): Promise<T> {
  const headers = new Headers(options.headers)
  headers.set('Content-Type', 'application/json')
  if (token) {
    headers.set('token', token)
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  })

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`)
  }

  return response.json() as Promise<T>
}

export async function uploadFile(path: string, uri: string, token?: string | null): Promise<string> {
  const formData = new FormData()
  formData.append('file', {
    uri,
    name: `upload-${Date.now()}.jpg`,
    type: 'image/jpeg'
  } as unknown as Blob)

  const headers: Record<string, string> = {}
  if (token) {
    headers.token = token
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers,
    body: formData
  })

  if (!response.ok) {
    throw new Error(`Upload failed: ${response.status}`)
  }

  return response.text()
}
