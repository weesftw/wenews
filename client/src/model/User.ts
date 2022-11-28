export interface User {
  banned: boolean
  username: string
  imageUrl: string
  roles: string[]
  token: string
  refreshToken: string
}
