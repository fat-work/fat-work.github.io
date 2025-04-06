import { createRouter, createWebHistory } from "vue-router";
import { ElMessage } from "element-plus";
import { KeepAlive } from "vue";
import { useRouterStore } from '@/store/routerStore';
const views = import.meta.glob("@/views/**/*.vue");

const constRoutes = [
  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
  },
  {
    path: "/404",
    component: () => import("@/views/components/404.vue"),
  },
  {
    path: "/screen",
    component: () => import("@/views/screen/index.vue"),
  },

  {
    path: "/",
    name: "layout",
    component: () => import("@/layout/index.vue"),
    meta: {
      title: "layout",
      KeepAlive: true
    },
    children: [
    ]
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: constRoutes,
});

// 动态添加路由函数
export function addDynamicRoutes(menus) {

    const newRoutes = [];
    newRoutes.push({
        path: "/changePassword",
        component: () => import("@/views/base/changePassword.vue"),
        children: [
        ]
    });
    menus.forEach(menu => {
        const normalizedUrl = menu.url.replace(/\/+/g, '/');
        const routesArray = normalizedUrl.split('/').filter(Boolean);
        const childRoute = routesArray[routesArray.length - 1];
        
        let path = normalizedUrl.replace('/', '-')
         let tempRoute  = {
            path: `/${path}`,
            meta: {
                title: menu.title,
                KeepAlive: true
            },
            component: views[`/src/views/${normalizedUrl}.vue`], // 确保路径正确
            // component: () => import(`/src/views/${normalizedUrl}.vue`), // 确保路径正确
            name: normalizedUrl.replace('/', '-'),
         }
         
         newRoutes.push(tempRoute);
    });
    newRoutes.forEach(route => {
        router.addRoute('layout', route); // 将子路由添加到 layout 路由下
      });
    console.log("已经设置了新的路由")
    const routerStore = useRouterStore();
    routerStore.setRoutes(menus);
}

/**
 * 前置路由守卫
 */
let registerRouterFresh = true;
router.beforeEach((to, from, next) => {
    const routerStore = useRouterStore();
    const menus = routerStore.loadRoutes();
    if(menus.length > 0){
        // 动态添加路由
        addDynamicRoutes(menus)
    }
    if(registerRouterFresh && to.matched.length === 0){
        registerRouterFresh = false;
        router.push(to.path)
    }
    console.log("to.matched.length", to);
    
    if(to.matched.length>=2){
        console.log(typeof(to.matched[0].components));
    console.log(typeof(to.matched[1].components));
    }
    
    if (to.matched.length>= 2 && to.path !== "/404" && ((typeof(to.matched[0].components)==='undefined') || (typeof(to.matched[1].components)==='undefined'))){
        router.push("/404");
    }
  if (to.path === "/login" || to.path === "/screen") {
    next();
  } else {
    if (window.sessionStorage.getItem("userId")) {
      console.log("~~~~~~~~~前置路由守卫~~~~~");
      next();
    } else {
      ElMessage({
        type: "warning",
        message: "请先登录",
        duration: 1000,
        showClose: true,
      });
      next("/login");
    }
  }
//   next();
});

/**
 * 后置路由守卫
 */
router.afterEach(() => {
  // ...
});

export default router;
