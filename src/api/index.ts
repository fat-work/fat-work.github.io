import http from '@/utils/requestUtil'
export const proxy_url = '/api/'

// 文件上传的action
export const uploadAction =  proxy_url+'/file/upload'

export const ApiManager = {
	
	post:function(url: string,data: any){
        return http.post(
            proxy_url+url,
            data,
            {
              headers: {
                'Content-Type': 'application/json;charset=utf-8'
              },
            }
          )
		  
	},
	get:function(url: string,parames: any){
		return http.get(proxy_url+url, {
            params: parames
        })
		  
	},
    uploadFile:function(data: any){
        const url = '/file/upload'
		return http.post(
            proxy_url+url,
            data,
            {
              headers: {
                'Content-Type': 'multipart/form-data'
              },
            }
          )
	},
    richTextImgUpload:function(data: any){
        const url = '/file/upload'
		return http.post(
            proxy_url+url,
            data,
            {
              headers: {
                'Content-Type': 'multipart/form-data'
              },
            }
          )
	},
    richTextVideoUpload: function(data: any){
        const url = '/file/upload'
		return http.post(
            proxy_url+url,
            data,
            {
              headers: {
                'Content-Type': 'multipart/form-data'
              },
            }
          )
	},
    
	download:async  function(url: string,data: any){
  
        const response:any = await http.post(proxy_url+url, data,
            {
                headers: {
                    'Content-type': 'application/json;charset=utf-8'
                },
                responseType: 'blob',
                withCredentials: true,
            }
        )
        const blob = new Blob([response],{type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'})
            const fileName = "导出的文件"
            const link = document.createElement('a')// 创建一个a标签
                    //     const link = document.createElement('a')// 创建一个a标签
            link.download = fileName// 设置a标签的下载属性
            link.style.display = 'none'// 将a标签设置为隐藏
            link.href = URL.createObjectURL(blob)// 把之前处理好的地址赋给a标签的href
            document.body.appendChild(link)// 将a标签添加到body中
            link.click()// 执行a标签的点击方法
            URL.revokeObjectURL(link.href) // 下载完成释放URL 对象
            document.body.removeChild(link)// 移除a标签
            return
    }
	
}

export default ApiManager
