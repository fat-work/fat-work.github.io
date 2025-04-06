import { defineStore } from 'pinia'
 
/**
 * 用户菜单管理仓库
 */
export const useMenuInfoStore = defineStore({
  id: 'useMenuInfoStore', // ID必填且唯一
  state: () => {
    return {
      menus: [] as any[],
    }
  },
  getters: {
 
  },
  actions: {
    setMenus(menus: Array<any>) {
      this.menus = menus
    },
  }
})