<template>
    <el-image :src="realSrc" fit="cover" :style="style" :preview-src-list="realSrcList">
        <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
        </div>
    </el-image>
</template>
<script>
function isExternal(path) {
    return /^(https?:|mailto:|tel:)/.test(path)
}

export default {
    name: 'ImagePreview',
    props: {
        src: {
            type: String,
            default: ''
        },
        width: {
            type: [Number, String],
            default: 'auto'
        },
        height: {
            type: [Number, String],
        }
    },
    computed: {
        realSrc() {
            if (!this.src || this.src == "null" || this.src == "") {
                return;
            }
            let data = JSON.parse(this.src);
            const result = data.map(i => i.url).join(',')
            let real_src = result.split(",")[0];
            if (isExternal(real_src)) {
                return real_src;
            }
            return real_src;
        },
        realSrcList() {
            if (!this.src || this.src == "null" || this.src == "") {
                return;
            }
            let data = JSON.parse(this.src);
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
    },
}

</script>