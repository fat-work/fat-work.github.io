import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from 'unplugin-auto-import/vite'
import { createHtmlPlugin } from "vite-plugin-html";
import { resolve } from "path";

//这个配置 为了在html中使用 环境变量
const getViteEnv = (mode, target) => {
  return loadEnv(mode, process.cwd())[target];
};

/**
 * 详情见 vitejs 文档：https://vitejs.dev/config/
 */
 export default defineConfig(({command, mode}) =>{
    return{
        plugins: [
          vue(),
          createHtmlPlugin({
            inject: {
              data: {
               title: getViteEnv(mode, "VITE_APP_TITLE"),
              },
            },
          }),
          AutoImport({
            include: [
              /\.[tj]sx?$/,
              /\.vue$/,
              /\.vue\?vue/,
              /\.md$/,
            ],
            imports: ['vue','vue-router', 'pinia', '@vueuse/core'],
            //注意这个配置和src同级
            dts: './auto-imports.d.ts'
          })
        ],
        base: "/", // 配置相对地址或绝对地址，此处应为绝对地址，若将 Web 部署到 Nginx 所在的目录为 nginx-1.17.8/html/xxx ，则这个 base 的值就为 /xxx/
        resolve: {
          alias: {
            "@": resolve(__dirname, "./src"),
            assets: resolve(__dirname, "./src/assets"),
          },
        },
        server: {
          host: "", // 主机
          port: 5173, // 端口
          proxy: {
            // 默认服务端接口地址
            "/api": {
              target: "http://127.0.0.1:3000/",
              changeOrigin: true,
              secure: false,
              rewrite: (path) => path.replace(/^\/api/, ""),
            },
          },
        },
      }

 } );
