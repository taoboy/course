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
            是分页组件暴露出来的一个回调方法，后面的list，是course组件的list方法。
        -->
        <pagination ref="pagination" v-bind:list="list"></pagination>


        <div class="row">
            <div v-for="course in courses" class="col-md-2">
                <div class="thumbnail search-thumbnail">
                    <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
                    <img v-show="course.image"  class="media-object" v-bind:src="course.image"  />
                    <div class="caption">
                        <div class="clearfix">
                            <span class="pull-right label label-grey info-label">
                                {{COURSE_LEVEL | optionKV(course.level)}}
                            </span>
                            <span class="pull-right label label-grey info-label">
                                {{COURSE_CHARGE | optionKV(course.charge)}}
                            </span>
                            <span class="pull-right label label-grey info-label">
                                {{COURSE_STATUS | optionKV(course.status)}}
                            </span>
                        </div>

                        <h3 class="search-title">
                            <a href="#" class="blue">{{course.name}}</a>
                        </h3>
                        <p>
                            <span class="blue bolder bigger-150">{{course.price}}&nbsp;<i class="fa fa-rmb"></i></span>&nbsp;
                        </p>
                        <p>{{course.summary}}</p>

                        <p>
                            <span class="badge badge-info">{{course.id}}</span>
                            <span class="badge badge-info">排序：{{course.sort}}</span>
                            <span class="badge badge-info">{{course.time | formatSecond}}</span>
                        </p>
                        <p>
                            <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                                编辑
                            </button>&nbsp;

                            <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                                删除
                            </button>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!--<table id="simple-table" class="table  table-bordered table-hover">-->
        <!--<thead>-->
        <!--<tr>-->
        <!--<th>id</th>-->
        <!--<th>名称</th>-->
        <!--<th>概述</th>-->
        <!--<th>时长</th>-->
        <!--<th>价格（元）</th>-->
        <!--<th>封面</th>-->
        <!--<th>级别</th>-->
        <!--<th>收费</th>-->
        <!--<th>状态</th>-->
        <!--<th>报名数</th>-->
        <!--<th>顺序</th>-->
        <!--<th>操作</th>-->
        <!--</tr>-->
        <!--</thead>-->

        <!--<tbody>-->

        <!--<tr v-for="course in courses">-->
                <!--<td>{{course.id}}</td>-->
                <!--<td>{{course.name}}</td>-->
                <!--<td>{{course.summary}}</td>-->
                <!--<td>{{course.time}}</td>-->
                <!--<td>{{course.price}}</td>-->
                <!--<td>{{course.image}}</td>-->
                <!--<td>{{COURSE_LEVEL | optionKV(course.level)}}</td>-->
                <!--<td>{{COURSE_CHARGE | optionKV(course.charge)}}</td>-->
                <!--<td>{{COURSE_STATUS | optionKV(course.status)}}</td>-->
                <!--<td>{{course.enroll}}</td>-->
                <!--<td>{{course.sort}}</td>-->

            <!--<td>-->
                <!--<div class="hidden-sm hidden-xs btn-group">-->
                    <!--<button v-on:click="edit(course)" class="btn btn-xs btn-info">-->
                        <!--<i class="ace-icon fa fa-pencil bigger-120"></i>-->
                    <!--</button>-->

                    <!--<button v-on:click="del(course.id)" class="btn btn-xs btn-danger">-->
                        <!--<i class="ace-icon fa fa-trash-o bigger-120"></i>-->
                    <!--</button>-->
                <!--</div>-->
            <!--</td>-->
        <!--</tr>-->



        <!--</tbody>-->
    <!--</table>-->


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
                                          <label class="col-sm-2 control-label">名称</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.name" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">概述</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.summary" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">时长</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.time" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">价格（元）</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.price" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">封面</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.image" class="form-control">
                                          </div>
                                      </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">级别</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.level" class="form-control">
                                                <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">收费</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.charge" class="form-control">
                                                <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">状态</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.status" class="form-control">
                                                <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">报名数</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.enroll" class="form-control">
                                          </div>
                                      </div>
                                      <div class="form-group">
                                          <label class="col-sm-2 control-label">顺序</label>
                                          <div class="col-sm-10">
                                              <input v-model="course.sort" class="form-control">
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
        name: 'business-course',
        components: {Pagination},
        data:function(){
            return {
                course:{},
                courses:[],
                        COURSE_LEVEL: COURSE_LEVEL,
                        COURSE_CHARGE: COURSE_CHARGE,
                        COURSE_STATUS: COURSE_STATUS,
            }
        },

        mounted:function(){
            //this.$parent.activeSidebar("business-course-sidebar")
            let _this = this;
            _this.$refs.pagination.size=5;
            _this.list(1);
        },
        methods: {
            add(){
                let _this = this;
                _this.course={};
                $("#form-modal").modal("show");
            },

            edit(course){
                let _this = this;
                _this.course =  $.extend({},course);
                $("#form-modal").modal("show");
            },

            list(page){
                let _this = this;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER  + '/business/admin/course/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                })
                    .then((response) => {
                        Loading.hide();

                        let resp = response.data;
                        _this.courses = resp.content.list;
                        _this.$refs.pagination.render(page,resp.content.total)
                    })
            },

            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                      || !Validator.require(_this.course.name, "名称")
                      || !Validator.length(_this.course.name, "名称", 1, 50)
                      || !Validator.length(_this.course.summary, "概述", 1, 2000)
                      || !Validator.length(_this.course.image, "封面", 1, 100)
                ) {
                    return;
                }


                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save',_this.course)
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
                Confirm.show("删除课程后不可恢复，确认删除!",function () {
                    Loading.show();
                    _this.$ajax.delete(process.env.VUE_APP_SERVER  + '/business/admin/course/delete/' + id)
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
<style scoped>
    .caption h3 {
        font-size: 20px;
    }
</style>