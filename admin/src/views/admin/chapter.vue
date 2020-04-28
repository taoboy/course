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
            是分页组件暴露出来的一个回调方法，后面的list，是chapter组件的list方法。
        -->
        <pagination ref="pagination" v-bind:list="list"></pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>
            <th>ID</th>
            <th>名称</th>
            <th>课程ID</th>
            <th>操作</th>
            <th></th>
        </tr>
        </thead>

        <tbody>

        <tr v-for="chapter in chapters">
            <td>{{chapter.id}}</td>
            <td>{{chapter.name}}</td>
            <td>{{chapter.courseId}}</td>
            <td>
                <div class="hidden-sm hidden-xs btn-group">
                    <button class="btn btn-xs btn-success">
                        <i class="ace-icon fa fa-check bigger-120"></i>
                    </button>

                    <button class="btn btn-xs btn-info">
                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                    </button>

                    <button class="btn btn-xs btn-danger">
                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                    </button>

                    <button class="btn btn-xs btn-warning">
                        <i class="ace-icon fa fa-flag bigger-120"></i>
                    </button>
                </div>

                <div class="hidden-md hidden-lg">
                    <div class="inline pos-rel">
                        <button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
                            <i class="ace-icon fa fa-cog icon-only bigger-110"></i>
                        </button>

                        <ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
                            <li>
                                <a href="#" class="tooltip-info" data-rel="tooltip" title="View">
                                                                    <span class="blue">
                                                                        <i class="ace-icon fa fa-search-plus bigger-120"></i>
                                                                    </span>
                                </a>
                            </li>

                            <li>
                                <a href="#" class="tooltip-success" data-rel="tooltip" title="Edit">
                                                                    <span class="green">
                                                                        <i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
                                                                    </span>
                                </a>
                            </li>

                            <li>
                                <a href="#" class="tooltip-error" data-rel="tooltip" title="Delete">
                                                                    <span class="red">
                                                                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                                                    </span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </td>
        </tr>



        </tbody>
    </table>


        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">表单</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input class="form-control" placeholder="名称">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label">课程ID</label>
                                <div class="col-sm-10">
                                    <input class="form-control" placeholder="课程ID">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary">保存</button>
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
        name: 'chapter',
        components: {Pagination},
        data:function(){
            return {
                chapters:[]
            }
        },

        mounted:function(){
            //this.$parent.activeSidebar("business-chapter-sidebar")
            let _this = this;
            _this.$refs.pagination.size=5;
            _this.list(1);
        },
        methods: {
            add(){
                let _this = this;
                $(".modal").modal("show");
            },
            list(page){
                let _this = this;
                _this.$ajax.post('http://127.0.0.1:9000/business/admin/chapter/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                })
                    .then((response) => {
                        console.log("查询大章列表结果：",response);
                        _this.chapters = response.data.list;
                        _this.$refs.pagination.render(page,response.data.total)
                    })
            }
        }
    }
</script>