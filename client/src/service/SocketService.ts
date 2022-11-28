import {User} from 'src/model/User';
import moment from 'moment';

export enum SocketEvent {
  NEW_NEWS = 'NEW_NEWS', // send to all users new news when new news is created
  USER_JOINED = 'USER_JOINED', // send to all users when a new user joins
  USER_LEFT = 'USER_LEFT', // send to all users when a user leaves
  LOAD_NEWS = 'LOAD_NEWS', // send to a user when he joins
  BAN = 'BAN' // send to a user when he is banned
}

interface SocketResponse {
  event: SocketEvent
  properties: SocketProperties
  content: SocketContent
}

interface SocketProperties {
  news: News[]
  users: Socket[]
}

export interface SocketContent {
  socket: Socket
  content: string
  dateTime: string
  bgColor: string
}

export interface Socket {
  user: UserSocket
  sessionId: string
}

export interface UserSocket {
  username: string
  imageUrl: string
  roles: Roles[]
  banned: boolean
}

interface Roles {
  name: string
}

export interface News {
  id: string
  url: string
  title: string
  author: string
  category: string
  urlToImage: string
  description: string
  publishedAt: string
}

class SocketService {
  connect(user: User | undefined, category: string) {
    const socket = new WebSocket('ws://' + window.location.host + '/core/ws/' + user?.username + '/' + category);
    if (socket.readyState != WebSocket.CLOSED) {
      return socket
    }

    throw new Error('Socket is down')
  }

  parse(content: string) {
    const parsed: SocketResponse = JSON.parse(content);
    const value = new Date(parsed.content.dateTime)
    parsed.content.dateTime = moment(value).format('MMMM Do YYYY, h:mm:ss a')

    const roles = parsed.content?.socket?.user?.roles;
    if(roles != null) {
      roles.forEach((userRole) => {
        if(userRole.name === 'ROLE_ADMIN') {
          parsed.content.bgColor = 'red-5'
        } else if (userRole.name === 'ROLE_MOD') {
          parsed.content.bgColor = 'purple-5'
        } else if (userRole.name === 'ROLE_PRO') {
          parsed.content.bgColor = 'orange-5'
        } else {
          parsed.content.bgColor = 'grey-5'
        }
      })
    }

    const news = parsed.properties?.news;
    if (news != null)
      news.forEach(value => {
        const value1 = value.publishedAt
        value.publishedAt = moment(new Date(value1)).format('MMMM Do YYYY, h:mm:ss a')
      });

    return parsed;
  }
}

export const socketService = new SocketService();
