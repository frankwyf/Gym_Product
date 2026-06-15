export type RootStackParamList = {
  MainTabs: undefined
  Login: undefined
  Register: undefined
  ForgotPassword: undefined
  CourseDetail: { courseId?: number; title?: string }
  VenueDetail: { venueId?: number; facilityId?: number; title?: string }
  PostDetail: { postId?: number; title?: string }
  SendPost: undefined
  Wallet: undefined
  Orders: undefined
  Search: { term?: string }
  Notices: { title?: string; description?: string } | undefined
  Video: { title?: string; description?: string } | undefined
  CheckIn: { title?: string; description?: string } | undefined
  Addresses: { title?: string; description?: string } | undefined
}

export type MainTabParamList = {
  Home: undefined
  Reservation: undefined
  Community: undefined
  Courses: undefined
  Profile: undefined
}
