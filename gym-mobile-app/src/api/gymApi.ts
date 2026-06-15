import { apiRequest, uploadFile } from './client'
import type { Account, CartItem, Coach, Comment, Course, CustomerProfile, Facility, Notice, Post, Slide, Venue } from '../types/models'

type ApiEnvelope<T> = {
  code: number
  data: T
  msg?: string
  captchaOnOff?: boolean
  img?: string
  uuid?: string
}

export const gymApi = {
  login: (username: string, password: string) =>
    apiRequest<ApiEnvelope<{ token: string }>>('loginCus/login', {
      method: 'POST',
      body: JSON.stringify({ username, password })
    }),
  logout: (token: string) => apiRequest<ApiEnvelope<unknown>>('loginCus/logout', { method: 'GET' }, token),
  homeSlides: () => apiRequest<ApiEnvelope<Slide[]>>('until/homeslides'),
  notices: () => apiRequest<ApiEnvelope<Notice[]>>('until/notices'),
  facilities: () => apiRequest<ApiEnvelope<Facility[]>>('until/facilities'),
  coaches: () => apiRequest<ApiEnvelope<Coach[]>>('until/coaches'),
  courseSlides: () => apiRequest<ApiEnvelope<Slide[]>>('until/courseSlides'),
  allCourses: () => apiRequest<ApiEnvelope<Course[]>>('until/allCourses'),
  allVenues: () => apiRequest<ApiEnvelope<Facility[]>>('facility/getAllFacilities'),
  venueById: (vid: number) => apiRequest<ApiEnvelope<Array<{ venue: Venue; cap: number[] }>>>(`venue/getById?vid=${vid}`),
  allPosts: () => apiRequest<ApiEnvelope<Post[]>>('until/allPosts'),
  postDetails: (postId: number) => apiRequest<ApiEnvelope<Post>>(`until/specificPost?postID=${postId}`),
  postComments: (postId: number) => apiRequest<ApiEnvelope<Comment[]>>(`until/postComment?postID=${postId}`),
  addPost: (token: string, content: string, media?: string) =>
    apiRequest<ApiEnvelope<unknown>>('posts/add', {
      method: 'POST',
      body: JSON.stringify({ content, media })
    }, token),
  addComment: (token: string, postId: number, content: string) =>
    apiRequest<ApiEnvelope<unknown>>('posts/postComment', {
      method: 'POST',
      headers: {
        content,
        PostID: String(postId)
      }
    }, token),
  uploadPostImage: (token: string, uri: string) => uploadFile('file/upload/posts', uri, token),
  accounts: (token: string) => apiRequest<ApiEnvelope<Account[]>>('account/page', { method: 'GET' }, token),
  customerInfo: (token: string) => apiRequest<ApiEnvelope<{ customer: CustomerProfile }>>('customer/CheckInformation', { method: 'GET' }, token),
  upgradeMembership: (token: string, aid: number, type: string) =>
    apiRequest<ApiEnvelope<unknown>>(`customer/vipMem?aid=${aid}&type=${encodeURIComponent(type)}`, { method: 'POST' }, token),
  createAccount: (token: string, payload: Partial<Account>) =>
    apiRequest<ApiEnvelope<unknown>>('account/add', {
      method: 'POST',
      body: JSON.stringify(payload)
    }, token),
  chargeAccount: (token: string, aid: number, balance: number) =>
    apiRequest<ApiEnvelope<unknown>>('account/edit', {
      method: 'POST',
      body: JSON.stringify({ aid, balance })
    }, token),
  deleteAccount: (token: string, aid: number) =>
    apiRequest<ApiEnvelope<unknown>>('account/delete', {
      method: 'POST',
      body: JSON.stringify({ aid })
    }, token),
  paidReservations: (token: string) => apiRequest<ApiEnvelope<CartItem[]>>('reservation/getPaid', { method: 'GET' }, token),
  bills: (token: string) => apiRequest<ApiEnvelope<unknown[]>>('bill/showall', { method: 'GET' }, token),
  userId: (token: string) => apiRequest<ApiEnvelope<number>>('customer/getuid', { method: 'GET' }, token)
}
