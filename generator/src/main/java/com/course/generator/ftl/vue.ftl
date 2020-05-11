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
            是分页组件暴露出来的一个回调方法，后面的list，是${domain}组件的list方法。
        -->
        <pagination ref="pagination" v-bind:list="list"></pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>
            <#list fieldList as field>
                <th>${field.nameCn}</th>

            </#list>

        </tr>
        </thead>

        <tbody>

        <tr v-for="${domain} in ${domain}s">
            <#list fieldList as field>
                <td>{{${domain}.${field.nameHump}}}</td>
            </#list>

            <td>
                <div class="hidden-sm hidden-xs btn-group">
                    <button v-on:click="edit(${domain})" class="btn btn-xs btn-info">
                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                    </button>

                    <button v-on:click="del(${domain}.id)" class="btn btn-xs btn-danger">
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

                            <#list fieldList as field>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">${field.nameCn}</label>
                                    <div class="col-sm-10">
                                        <input v-model="${domain}.${field.nameHump}" class="form-control">
                                    </div>
                                </div>
                            </#list>
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
        name: '${domain}',
        components: {Pagination},
        data:function(){
            return {
                ${domain}:{},
                ${domain}s:[]
            }
        },

        mounted:function(){
            //this.$parent.activeSidebar("${module}-${domain}-sidebar")
            let _this = this;
            _this.$refs.pagination.size=5;
            _this.list(1);
        },
        methods: {
            add(){
                let _this = this;
                _this.${domain}={};
                $("#form-modal").modal("show");
            },

            edit(${domain}){
                let _this = this;
                _this.${domain} =  $.extend({},${domain});
                $("#form-modal").modal("show");
            },

            list(page){
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER  + '/${module}/admin/${domain}/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                })
                    .then((response) => {
                        Loading.hide();

                        let resp = response.data;
                        _this.${domain}s = resp.content.list;
                        _this.$refs.pagination.render(page,resp.content.total)
                    })
            },

            save(){
                let _this = this;

                // 保存校验


                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/${module}/admin/${domain}/save',_this.${domain})
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
                Confirm.show("删除${tableNameCn}后不可恢复，确认删除!",function () {
                    Loading.show();
                    _this.$ajax.delete(process.env.VUE_APP_SERVER  + '/${module}/admin/${domain}/delete/' + id)
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