// stores/routerStore.js
import { defineStore } from 'pinia';

export const useRouterStore = defineStore('routerStore', {
  state: () => ({
    routes: [] as any[] // 存储动态路由
  }),
  actions: {
    setRoutes(routes: any[]) {
      this.routes = routes;
      localStorage.setItem('dynamicRoutes', JSON.stringify(routes));
    },
    loadRoutes() {
      const storedRoutesString = localStorage.getItem('dynamicRoutes');
      
      let storedRoutes: string[] = [];
      if (storedRoutesString) {
        storedRoutes = JSON.parse(storedRoutesString);
      }
      console.log('storedRoutes', storedRoutes);
      this.routes = storedRoutes;
      return storedRoutes;
    }
  }
});
