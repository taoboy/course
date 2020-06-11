<template>
    <div>
        <p>
            &nbsp;
            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-refresh"></i>
            刷新
        </button>

        </p>

        <!--第二步：放在需要显示的地方
            v-bind:list="list",前面的list，
            是分页组件暴露出来的一个回调方法，后面的list，是file组件的list方法。
        -->
        <pagination ref="pagination" v-bind:list="list"></pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
                <tr>
                <th>id</th>
                <th>相对路径</th>
                <th>文件名</th>
                <th>后缀</th>
                <th>大小</th>
                <th>用途</th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="file in files">
                    <td>{{file.id}}</td>
                    <td>{{file.path}}</td>
                    <td>{{file.name}}</td>
                    <td>{{file.suffix}}</td>
                    <td>{{file.size | formatFileSize}}</td>
                    <td>{{FILE_USE | optionKV(file.use)}}</td>
                </tr>
            </tbody>
        </table>
    </div>

</template>

<script>
    //第一步：引入组件
    import Pagination from "../../components/pagination";
    export default {
        name: 'file-file',
        components: {Pagination},
        data:function(){
            return {
                file:{},
                files:[],
                        FILE_USE: FILE_USE,
            }
        },

        mounted:function(){
            //this.$parent.activeSidebar("file-file-sidebar")
            let _this = this;
            _this.$refs.pagination.size=5;
            _this.list(1);
        },
        methods: {

            list(page){
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER  + '/file/admin/file/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                })
                .then((response) => {
                    Loading.hide();

                    let resp = response.data;
                    _this.files = resp.content.list;
                    _this.$refs.pagination.render(page,resp.content.total)
                })
            },
        }
    }
</script>