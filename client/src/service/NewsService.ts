import {api} from 'boot/axios';

interface CreateNews {
  title: string
  description: string
  author: string
  url: string
  publishedAt: string | null
  urlToImage: string
  category: string
}

interface CreateCategory {
  name: string
  password: string | null
}

interface NewsResponse {
  message: string
}

export default class NewsService {
  createCategory(category: CreateCategory) {
    return new Promise<NewsResponse>(function (myResolve, myReject) {
      return api.post('/news/v1/categories', category).then(() => {
        myResolve({ message: 'Category created' } )
      }).catch(reason => {
        myReject({ message: reason.response.data.errors[0] } )
      });
    })
  }

  createNews(news: CreateNews) {
    return new Promise<NewsResponse>(function (myResolve, myReject) {
      return api.post('/news/v1/news', news).then(() => {
        myResolve({ message: 'News created & published' } )
      }).catch(reason => {
        myReject({ message: reason.response.data.errors[0] } )
      });
    })
  }
}
