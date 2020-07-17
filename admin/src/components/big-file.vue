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

            console.log(file);
            /*
            name: "VID_20200602_172059.mp4"
            lastModified: 1593098448414
            lastModifiedDate: Thu Jun 25 2020 23:20:48 GMT+0800 (中国标准时间) {}
            webkitRelativePath: ""
            size: 71831281
            type: "video/mp4"
             */
            let key = hex_md5(file);
            let key10 = parseInt(key,16);
            let key62 = Tool._10to62(key10);
            console.log(key,key10,key62);
            /*
            d41d8cd98f00b204e9800998ecf8427e
            2.8194976848941264e+38
            6sfSqfOwzmik4A4icMYuUe
             */

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

            let size = file.size;
            let shardTotal = Math.ceil(size / shardSize);//总片数


            let param = {

                'shardIndex': shardIndex,
                'shardSize': shardSize,
                'shardTotal': shardTotal,
                'use': _this.use,
                'name': file.name,
                'suffix': suffix,
                'size': file.size,
                'key': key62
            };

            _this.upload(param);
        },

        upload: function (param) {
             let _this = this;
             let shardIndex = param.shardIndex;
             let shardTotal = param.shardTotal;
             let shardSize = param.shardSize
            let fileShard = _this.getFileShard(shardIndex,shardSize );
            //将图片转为base64进行传输
            let fileReader = new FileReader();
            fileReader.onload = function (e) {
                let base64 = e.target.result;

                param.shard = base64

                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', param).then((response) => {
                    Loading.hide();
                    let resp = response.data;
                    console.log("上传文件成功：" + resp);
                    if (shardIndex < shardTotal) {
                        //上传下一个分片
                        param.shardIndex = param.shardIndex+1;
                        _this.upload(param);
                    } else {
                        _this.afterUpload(resp);
                    }
                    $("#" + _this.inputId + "-input").val("");
                });
            };
            fileReader.readAsDataURL(fileShard)
        },
        getFileShard: function (shardIndex, shardSize) {
            let _this = this;
            let file = _this.$refs.file.files[0];
            let start = (shardIndex - 1) * shardSize; //当前分片起始位置 0 20 40
            let end = Math.min(file.size, start + shardSize); //当前分片结束位置
            let fileShard = file.slice(start, end); //从文件中截取当前的分片数据
            return fileShard;
        },

        selectFile () {
            let _this = this;
            $("#" + _this.inputId + "-input").trigger("click");
        }
    }
  }
</script>