<!--
 * 检查项目管理列表
 *
-->
<template>
    <div>
        <el-card>
            <!--搜索栏-->
            <el-row type="flex">
                <el-col :span="8">
                    <el-input v-model="query" placeholder="请输入项目名称查询">
                        <el-button
                            round
                            slot="append"
                            icon="iconfont icon-search-button"
                            @click="requestChecks"
                        ></el-button>
                    </el-input>
                </el-col>
                <el-col :span="1"></el-col>
            </el-row>
            <el-row type="flex">
                <el-col :span="6">
                    <br/>
                    <el-button size="small" type="success" style="font-size: 15px" @click="addFormVisible = true">
                    <i class="iconfont icon-add-button" style="font-size: 15px;"></i>
                        增加项目
                    </el-button>
                </el-col>
            </el-row>

            <!--表格 -->
            <el-table :data="checkData" size="small" stripe style="width: 100%" border>
                <el-table-column label="编号" prop="chId"/>
                <el-table-column label="项目" prop="chName"/>
                <el-table-column label="价格/元"prop="chPrice"/>
                <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            size="small"
                            style="font-size: 14px"
                            type="success"
                            @click="modifyDialog(scope.row.chId)"
                        ><i class="iconfont icon-edit-button" style="font-size: 12px;"></i></el-button>
                        <el-button
                            size="small"
                            style="font-size: 14px"
                            type="danger"
                            @click="deleteDialog(scope.row.chId)"
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

        <!-- 增加检查项目对话框 -->
        <el-dialog title="增加检查项目" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="编号" prop="chId" label-width="80px">
                    <el-input v-model.number="addForm.chId"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="chName" label-width="80px">
                    <el-input v-model="addForm.chName"></el-input>
                </el-form-item>
                <el-form-item label="价格" prop="chPrice" label-width="80px">
                    <el-input v-model="addForm.chPrice"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="addFormVisible = false" style="font-size: 18px;">
                  <i class="iconfont icon-cancel-button" style="font-size: 20px;"></i> 取 消
                </el-button>
                <el-button size="mini" type="primary" @click="addCheck('ruleForm')"style="font-size: 18px;">
                  <i class="iconfont icon-sure-button" style="font-size: 20px;"></i> 确 定
                </el-button>
            </div>
        </el-dialog>

        <!-- 修改检查项目药物对话框 -->
        <el-dialog title="修改检查项目" :visible.sync="modifyFormVisible">
            <el-form :model="modifyForm" :rules="rules" ref="ruleForm">
                <el-form-item label="编号" prop="chId" label-width="80px">
                    <el-input
                        v-model.number="modifyForm.chId"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="chName" label-width="80px">
                    <el-input v-model="modifyForm.chName"></el-input>
                </el-form-item>
                <el-form-item label="价格" prop="chPrice" label-width="80px">
                    <el-input v-model="modifyForm.chPrice"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini"  @click="modifyFormVisible = false" style="font-size: 18px;">
                  <i class="iconfont icon-cancel-button" style="font-size: 20px;"></i> 取 消
                </el-button>
                <el-button size="mini"  type="primary" @click="modifyCheck('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定
                </el-button>
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
            checkData: [],
            total: 3,
            addFormVisible: false,
            addForm: {},
            rules: {
                chId: [
                    { required: true, message: "请输入编号", trigger: "blur" },
                    {
                        type: "number",
                        message: "账号必须数字类型",
                        trigger: "blur",
                    },
                ],
                chName: [
                    { required: true, message: "请输入名称", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "账号必须是1到50个字符",
                        trigger: "blur",
                    },
                ],
                chPrice: [
                    { required: true, message: "请输入单价", trigger: "blur" },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        //点击修改药物信息
        modifyCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("check/modifyCheck", {
                            params: {
                                chId: this.modifyForm.chId,
                                chName: this.modifyForm.chName,
                                chPrice: this.modifyForm.chPrice,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("修改信息失败！");
                            this.modifyFormVisible = false;
                            this.requestChecks();
                            this.$message.success("修改检查项目信息成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //打开修改对话框
        modifyDialog(id) {
            request
                .get("check/findCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("请求数据失败");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                });
        },
        //删除检查操作
        deleteCheck(id) {
            request
                .get("check/deleteCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    this.requestChecks();
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该检查项目信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteCheck(id);
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
        addCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("check/addCheck", {
                            params: {
                                chId: this.addForm.chId,
                                chName: this.addForm.chName,
                                chPrice: this.addForm.chPrice,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "编号不合法或已被占用！"
                                );
                            this.addFormVisible = false;
                            this.requestChecks();
                            this.$message.success("增加检查项目成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestChecks();
        },
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestChecks();
        },
        //加载检查列表
        requestChecks() {
            request
                .get("check/findAllChecks", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.checkData = res.data.data.checks;
                    this.total = res.data.data.total;
                });
        },
    },
    created() {
        this.requestChecks();
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
