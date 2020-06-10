<template>
    <div>
        <p>
            <button v-on:click="add()" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-edit"></i>
                添加
            </button>
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
        <th>操作</th>
        </tr>
        </thead>

        <tbody>

        <tr v-for="file in files">
                <td>{{file.id}}</td>
                <td>{{file.path}}</td>
                <td>{{file.name}}</td>
                <td>{{file.suffix}}</td>
                <td>{{file.size}}</td>
                <td>{{FILE_USE | optionKV(file.use)}}</td>

            <td>
                <div class="hidden-sm hidden-xs btn-group">
                    <button v-on:click="edit(file)" class="btn btn-xs btn-info">
                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                    </button>

                    <button v-on:click="del(file.id)" class="btn btn-xs btn-danger">
                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                    </button>
                </div>
            </td>
        </tr>



        </tbody>
    </table>


        <!-- Modal -->
        <div class="modal fade" id="form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">表单</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">

                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">相对路径</label>
                                          <div class="col-sm-10">
                                              <input v-model="file.path" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">文件名</label>
                                          <div class="col-sm-10">
                                              <input v-model="file.name" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">后缀</label>
                                          <div class="col-sm-10">
                                              <input v-model="file.suffix" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">大小</label>
                                          <div class="col-sm-10">
                                              <input v-model="file.size" class="form-control">
                                          </div>
                                      </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">用途</label>
                                        <div class="col-sm-10">
                                            <select v-model="file.use" class="form-control">
                                                <option v-for="o in FILE_USE" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" v-on:click="save">保存</button>
                    </div>
                </div>
            </div>
        </div>
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
            add(){
                let _this = this;
                _this.file={};
                $("#form-modal").modal("show");
            },

            edit(file){
                let _this = this;
                _this.file =  $.extend({},file);
                $("#form-modal").modal("show");
            },

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

            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                      || !Validator.require(_this.file.path, "相对路径")
                      || !Validator.length(_this.file.path, "相对路径", 1, 100)
                      || !Validator.length(_this.file.name, "文件名", 1, 100)
                      || !Validator.length(_this.file.suffix, "后缀", 1, 10)
                ) {
                    return;
                }


                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/file/save',_this.file)
                    .then((response) => {
                        Loading.hide();

                        let resp = response.data;
                        if (resp.success){
                            $("#form-modal").modal("hide");
                            _this.list(1);
                            Toast.success("保存成功！");
                        }else {
                            Toast.warning(resp.message)
                        }
                    })
            },

            del(id){
                let _this = this;
                Confirm.show("删除文件后不可恢复，确认删除!",function () {
                    Loading.show();
                    _this.$ajax.delete(process.env.VUE_APP_SERVER  + '/file/admin/file/delete/' + id)
                        .then((response) => {
                            Loading.hide();
                            let resp = response.data;
                            if (resp.success){
                                _this.list(1);
                                Toast.success("删除成功")
                            }
                        })
                });
            }
        }
    }
</script>