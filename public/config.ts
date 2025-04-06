const toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'],        // 加粗，斜体，下划线，删除线
    ['blockquote', 'code-block'],                     //引用，代码块
    [{ 'header': 1 }, { 'header': 2 }],               // 几级标题
    [{ 'list': 'ordered' }, { 'list': 'bullet' }],    // 有序列表，无序列表
    [{ 'script': 'sub' }, { 'script': 'super' }],     // 下角标，上角标
    [{ 'indent': '-1' }, { 'indent': '+1' }],         // 缩进
    [{ 'direction': 'rtl' }],                         // 文字输入方向
    [{ 'size': ['small', false, 'large', 'huge'] }],  // 字体大小
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],        // 标题
    [{ 'color': [] }, { 'background': [] }],          // 颜色选择
    [{ 'font': ['SimSun', 'SimHei', 'Microsoft-YaHei', 'KaiTi', 'FangSong', 'Arial'] }], // 字体
    [{ 'align': [] }],    // 居中
    ['clean'],            // 清除样式,
    ['link', 'image']   // 上传图片、上传视频
  ]

// TODO 请替换为您的百度地图API密钥
const baiduMapKey = 'hW6K98irVtw3a88SeUjB3Hm7sFonTkNW'