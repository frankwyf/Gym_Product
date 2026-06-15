export type Slide = {
  id?: number
  image?: string
  profile?: string
  title?: string
  [key: string]: unknown
}

export type Notice = {
  nid?: number
  title?: string
  content?: string
  [key: string]: unknown
}

export type Coach = {
  cid?: number
  cname?: string
  profile?: string
  phone?: string
  [key: string]: unknown
}

export type Facility = {
  fid?: number
  fname?: string
  profile?: string
  [key: string]: unknown
}

export type Course = {
  cid?: number
  id?: number
  name?: string
  cname?: string
  profile?: string
  type?: string
  price?: number
  [key: string]: unknown
}

export type Venue = {
  vid?: number
  vname?: string
  price?: number
  profile?: string
  venue?: Venue
  [key: string]: unknown
}

export type Post = {
  pid?: number
  title?: string
  content?: string
  media?: string
  type?: string
  [key: string]: unknown
}

export type Comment = {
  id?: number
  content?: string
  [key: string]: unknown
}

export type CustomerProfile = {
  username?: string
  membership?: string
  profile?: string
  [key: string]: unknown
}

export type Account = {
  aid?: number
  balance?: number
  method?: string
  isActive?: boolean
  [key: string]: unknown
}

export type CartItem = {
  date?: string
  facility?: number
  venue?: number
  period?: number
  amount?: number
  type?: string
  pic?: string
  name?: string
  price?: number
  active?: boolean
}
