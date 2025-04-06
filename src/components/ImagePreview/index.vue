<template>
    <el-image :src="realSrc" fit="cover" :style="style" :preview-src-list="realSrcList">
        <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
        </div>
    </el-image>
</template>
<script>

import {proxy_url} from '@/api/index'
function isExternal(path) {
    return /^(https?:|mailto:|tel:)/.test(path)
}

export default {
    name: "ImagePreview",
    props: {
        src: {
            type: String,
            default: ""
        },
        width: {
            type: [Number, String],
            default: ""
        },
        height: {
            type: [Number, String],
            default: ""
        }
    },
    computed: {
        realSrc() {
            if (!this.src) {
                return;
            }
            let data = JSON.parse(this.src);
            // 如果data不是数组 就返回
            if (!Array.isArray(data)) {
                console.log("src 不是数组");
                return "";
            }
            console.log(data);
            const result = data.map(i => i.url).join(',')
            let real_src = result.split(",")[0];
            if (isExternal(real_src)) {
                return real_src;
            }
            return real_src;
        },
        realSrcList() {
            if (!this.src) {
                return;
            }
            let data = JSON.parse(this.src);
             // 如果data不是数组 就返回
             if (!Array.isArray(data)) {
                console.log("src 不是数组");
                return "";
            }
            const result = data.map(i => i.url).join(',')
            let real_src_list = result.split(",");
            let srcList = [];
            real_src_list.forEach(item => {
                if (isExternal(item)) {
                    return srcList.push(item);
                }
                return srcList.push(item);
            });
            return srcList;
        },
        realWidth() {
            return typeof this.width == "string" ? this.width : `${this.width}px`;
        },
        realHeight() {
            return typeof this.height == "string" ? this.height : `${this.height}px`;
        },
        style() {
            return {
                width: this.realWidth,
                height: this.realHeight
            };
        }
    }
}
</script>