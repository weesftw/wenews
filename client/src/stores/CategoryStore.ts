import {defineStore} from 'pinia';
import {api} from 'boot/axios';

interface CategoryState {
  category: Category[],
  externalCategory: Category[]
}

export interface Category {
  id: string
  name: string
  public: boolean
  enabled: boolean
  updatedAt: string
  createdAt: string
}

export const useCategoryStore = defineStore('category', {
  state: (): CategoryState => {
    return {
      category: [],
      externalCategory: []
    }
  },

  getters: {
    categories: (state) => state.category,
    externalCategories: (state) => state.externalCategory,
    allCategories: (state) => {
      const aux = [...state.externalCategory, ...state.category]
      return aux.map(item => item.name)
    }
  },

  actions: {
    async fetchCategory() {
      await api.get('/news/v1/categories').then(value => {
        this.category = value.data.internal;
        this.externalCategory = value.data.external;
      })
    }
  }
})
