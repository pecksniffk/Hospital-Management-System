<!--
 * 病床管理列表
 *
-->
<template>
    <div>
        <el-card>
            <!-- 搜索栏 -->
            <el-row type="flex">
                <el-col :span="8">
                    <el-input v-model="query" placeholder="请输入患者id查询">
                        <el-button
                            round
                            slot="append"
                            icon="iconfont icon-search-button"
                            @click="requestBeds"
                        ></el-button>
                    </el-input>
                </el-col>
                <el-col :span="1"></el-col>
            </el-row>

          <!--添加病床按钮-->
          <el-row type="flex">
                <el-col :span="6">
                    <br/>
                    <el-button
                        size="small"
                        type="success"
                        style="font-size: 15px"
                        @click="addFormVisible = true"
                    >
                    <i class="iconfont icon-add-button" style="font-size: 15px;"></i>
                        增加床位
                    </el-button >
                </el-col>
            </el-row>

            <!-- 表格 -->
            <el-table :data="bedData" size="small" stripe style="width: 100%" border>
                <el-table-column label="床号" prop="bId"/>
                <el-table-column label="患者id" prop="pId"/>
                <el-table-column label="医生id" prop="dId"/>
                <el-table-column label="开始时间" prop="bStart"/>
                <el-table-column label="申请理由"prop="bReason"/>
                <el-table-column label="状态" prop="bState">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.bState === 1" type="danger">
                          已占用
                        </el-tag>
                        <el-tag v-if="scope.row.bState === 0" type="primary">
                          空
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">

                        <el-button
                            size="mini"
                            style="font-size: 14px"
                            type="danger"
                            @click="emptyDialog(scope.row.bId)"
                        ><i class="iconfont icon-delete-button" style="font-size: 12px;"></i></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                background
                layout="total, sizes, prev, pager, next, jumper"
                :current-page="pageNumber"
                :page-size="size"
                :page-sizes="[1, 2, 4, 8, 16]"
                :total="total"
            >
            </el-pagination>
        </el-card>

        <!-- 增加床位对话框 -->
        <el-dialog title="增加床位" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="床号" prop="bId" label-width="80px">
                    <el-input v-model.number="addForm.bId"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="addBed('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
export default {
    name: "CheckList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            bedData: [],
            total: 3,
            addFormVisible: false,
            addForm: {},
            rules: {
                bId: [
                    { required: true, message: "请输入床号", trigger: "blur" },
                    {
                        type: "number",
                        message: "床号必须数字类型",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        //清空床位操作
        emptyBed(id) {
            request
                .get("bed/emptyBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
                });
        },
        //清空对话框
        emptyDialog(id) {
            this.$confirm("此操作将清空该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.emptyBed(id);
                    this.$message({
                        type: "success",
                        message: "清空成功!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消清空",
                    });
                });
        },

        //删除床位操作
        deleteBed(id) {
            request
                .get("bed/deleteBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteBed(id);
                    this.$message({
                        type: "success",
                        message: "删除成功!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除",
                    });
                });
        },
        //点击增加确认按钮
        addBed(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("bed/addBed", {
                            params: {
                                bId: this.addForm.bId,
                                pId: -1,
                                dId: -1,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("床号或已被占用！");
                            this.addFormVisible = false;
                            this.requestBeds();
                            this.$message.success("增加床位成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestBeds();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestBeds();
        },
        // 加载检查列表
        requestBeds() {
            request
                .get("bed/findAllBeds", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.bedData = res.data.data.beds;
                    this.total = res.data.data.total;
                });
        },
    },
    created() {
        this.requestBeds();
    },
};
</script>
<style scoped lang="scss">
.el-table {
    margin-top: 20px;
    margin-bottom: 20px;
}
.el-form {
    margin-top: 0;
}
</style>
