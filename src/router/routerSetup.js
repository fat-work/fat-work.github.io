import router from "@/router/index.js"; // 引入你的 router 实例
import { useRouterStore } from "@/store/routerStore";
const views = import.meta.glob("@/views/*.vue");
export function setupRoutes(newRoutes) {
    // 获取home的路由
    const homeRoute = router.getRoutes().find((route) => route.path === "/home");
    if (homeRoute) {
        // 移除home的子路由
        homeRoute.children.forEach((childRoute) => {
        router.removeRoute(childRoute.name);
        });
    }
    newRoutes.forEach((menu) => {
        const normalizedUrl = menu.url.replace(/\/+/g, '/'); // 规范化路径
        const routes = normalizedUrl.split('/').filter(Boolean);
        const parentRoute = routes.slice(0, -1).join('/'); // 父级路由
        const childRoute = routes[routes.length - 1]; // 子级路由
        
        router.addRoute('home', {
            path: `/${parentRoute}`,
            component: views[`/src/views/${parentRoute}.vue`],
            children: [
                {
                    path: `/${childRoute}`,
                    component: views[`/src/views/${normalizedUrl}.vue`],
                    name: childRoute.replace('/', '-'),
                }
            ]
        });
    });
  const routerStore = useRouterStore();
  routerStore.setRoutes(newRoutes);
}
