<template>
  <div>
      <button type=button v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
          <i class="ace-icon fa fa-upload"></i>
          {{text}}
      </button>
      <input class="hidden" type="file" ref="file" v-bind:id="inputId+'-input'" v-on:change="uploadFile()" >

  </div>
</template>

<script>
  export default {
    name: 'big-file',
    //props，用于父子组件传递数据，可以理解成组件可配置的属性
    props: {
      text:{
          default: "上传大文件"
      },
        inputId:{
          default: "file-upload"
      },
        suffixs:{
          default: []
      },
      use:{
          default:""
      },
      afterUpload: {
        type: Function,
        default: null
      },

      afterUploadContentFile:{
          type: Function,
          default: null
      }
    },
    data: function () {
      return {
      }
    },
    methods: {
        uploadFile(){
            let _this = this;
            let formData = new window.FormData();
            let file = _this.$refs.file.files[0];


            // 判断文件格式
            let suffixs = _this.suffixs;
            let fileName = file.name;
            let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
            let use = _this.use
            let validateSuffix = false;

            for (let i = 0; i < suffixs.length; i++) {
                if (suffixs[i].toLowerCase() === suffix) {
                    validateSuffix = true;
                    break;
                }
            }
            if (!validateSuffix) {
                Toast.warning("文件格式不正确！只支持上传：" + suffixs.join(","));
                $("#" + _this.inputId + "-input").val("");
                return;
            }

            //文件分片
            let shardSize = 40 * 1024 * 1024; //以20MB为一个分片
            let shardIndex = 1; //分片索引
            let start = shardIndex * shardSize; //当前分片起始位置 0 20 40
            // let end = start + shardSize; //分片结束的位置 1， 20~40 35
            let end = Math.min(file.size,start + shardSize); //当前分片结束位置
            let fileShard = file.slice(start,end); //从文件中截取当前的分片数据

            // key："file"必须和后端controller参数名一致

            formData.append('file',fileShard);
            formData.append('use',use);
            Loading.show();
            _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload',formData).then((response)=>{
                Loading.hide();
                let resp = response.data;
                console.log("上传文件成功：" + resp);
                _this.afterUpload(resp);
                $("#" + _this.inputId + "-input").val("");
            });
        },

        selectFile () {
            let _this = this;
            $("#" + _this.inputId + "-input").trigger("click");
        }
    }
  }
</script>