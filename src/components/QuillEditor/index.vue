<template>
    <div style="height: 80%;">
        <div style="height: 0px;">
            <el-upload :action="uploadFileAction" :show-file-list="false" :on-success="handleUploadSuccess" :auto-upload="true"
            ref="upload">
            </el-upload>
        </div>
        

        <QuillEditor ref="quillRef" v-model:content="content" :options="myOptions" contentType="html" @click="handClick"
            @textChange="handOnkeyup" @update:content="setValue()">
        </QuillEditor>
    </div>
</template>
<script setup lang="ts">
import { ApiManager, uploadAction } from '@/api/index' //引入上传图片的api
import { QuillEditor, Quill } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import Compressor from 'compressorjs';
import 'quill-image-uploader/dist/quill.imageUploader.min.css';

import ImageUploader from 'quill-image-uploader';
import BlotFormatter from 'quill-blot-formatter'
import { onMounted, ref } from 'vue';

import Link from './link'

const props = withDefaults(defineProps<{
    modelValue: any, // 双向绑定值
}>(), {
    modelValue: '', // 双向绑定值
})
const emit = defineEmits<{
    (e: 'update:modelValue', val: any): void
}>()
const content = ref<string>('')
const quillRef = ref<any>(null)

Quill.register("modules/imageUploader", ImageUploader);
Quill.register('modules/blotFormatter', BlotFormatter);
Quill.register(Link, true);

/**
 * 文件上传 begin
 */
const uploadFileAction = ref(uploadAction)
const handleUploadSuccess = (response: any, uploadFile: UploadFile, uploadFiles: UploadFiles) => {
    if (response.code == 200) {
        const url = response.data.url
        const frag = document.createDocumentFragment();
        const node = document.createElement("a");
        node.innerText = uploadFile.name
        node.setAttribute("href", url)
        frag.appendChild(node)
        lastRange.insertNode(frag);
    }
}

let lastRange: any = null
const handClick = () => {
    let selection: any = document.getSelection()
    // 保存最后的range对象
    lastRange = selection.getRangeAt(0)
    console.log('lastRange', lastRange)
}
const handOnkeyup = () => {
    let selection: any = document.getSelection()
    // 保存最后的range对象
    lastRange = selection.getRangeAt(0)
}
/**
 * 文件上传 end
 */

//富文本配置项，将模块功能一起写入到配置项内，也可以单独配置Modules
const myOptions = reactive({
    modules: {
        toolbar: {
            container: [
                ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
                ['blockquote', 'code-block'],
                ['image', 'uploadfile'],

                [{ 'header': 1 }, { 'header': 2 }],               // custom button values
                [{ 'list': 'ordered' }, { 'list': 'bullet' }, { 'list': 'check' }],
                [{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
                [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
                [{ 'direction': 'rtl' }],                         // text direction

                [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

                [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
                [{ 'font': [] }],
                [{ 'align': [] }],

                ['clean']],                                        // remove formatting button
            handlers: {
                uploadfile: function (value) {
                    console.log("uploadfile", value);
                    //自定义上传附件功能
                    setTimeout(() => {
                        if (value) {
                            document.querySelector(".el-upload input").click();
                        } else {
                            // _that.quill.format("uploadfile", false);
                        }
                    }, 100);
                },
            }
        },
        // 上传图片
        imageUploader: {
            upload: async (file: any) => {
                try {
                    const compressedFile: any = await compressImage(file); // 压缩图片
                    return new Promise((resolve, reject) => {
                        const formData = new FormData();
                        formData.append("file", compressedFile);
                        ApiManager.uploadFile(formData).then((res: any) => {
                            resolve(res.data.url);
                        }).catch(err => {
                            reject("Upload failed");
                            console.error("Error:", err)
                        })
                    })
                } catch (error) {
                    console.error('压缩和上传图像时出错:', error);
                }
            }
        },
        // 图片缩放
        blotFormatter: {
        },
    },
    placeholder: '请输入内容...'
})

onMounted(() => {
    // 设置自定义按钮
    const newFunctionButton = document.querySelector('.ql-uploadfile');
    if (newFunctionButton) {
        newFunctionButton.innerHTML = `<svg viewBox="0 0 18 18"> <line class="ql-stroke" x1="7" x2="11" y1="7" y2="11"></line> <path class="ql-even ql-stroke" d="M8.9,4.577a3.476,3.476,0,0,1,.36,4.679A3.476,3.476,0,0,1,4.577,8.9C3.185,7.5,2.035,6.4,4.217,4.217S7.5,3.185,8.9,4.577Z"></path> <path class="ql-even ql-stroke" d="M13.423,9.1a3.476,3.476,0,0,0-4.679-.36,3.476,3.476,0,0,0,.36,4.679c1.392,1.392,2.5,2.542,4.679.36S14.815,10.5,13.423,9.1Z"></path> </svg>`;
    }
    // 用于回显内容
    quillRef.value.setContents(props.modelValue)

})


// 图片压缩
const compressImage = (file: any) => {
    return new Promise((resolve, reject) => {
        new Compressor(file, {
            quality: 0.6, // 设置压缩质量
            // maxWidth: 400, // 设置图片最大宽度
            // maxHeight: 400, // 设置图片最大高度
            success(result) {
                resolve(result);
            },
            error(error) {
                reject(error);
            },
        });
    });
}

const setValue = () => { //用于设置双向绑定值
    const text = toRaw(quillRef.value).getHTML()
    emit('update:modelValue', text)
}

watch(() => props.modelValue, (val: any) => {
    if (val) {
        content.value = val //用于监听绑定值进行数据回填
        // console.log('content.value:', content.value)
        // toRaw(quillRef.value).setContents(val)
    } else {
        toRaw(quillRef.value).setContents('') //可用于弹窗使用富文本框关闭弹窗清除值
    }
})

</script>

<style>
.ql-container {
    width: 100%;
    /*通过calc函数动态设置富文本高度，前提父容器有具体高度*/
    height: calc(100% - 42px);
}
.ql-uploadfile {
    content: icon;
}
</style>