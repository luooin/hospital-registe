<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="值班人姓名" prop="keyWord1">
        <el-input
          v-model="queryParams.keyWord1"
          placeholder="请输入值班人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="值班开始时间" prop="schedulingStartTime">
        <el-date-picker clearable
          v-model="queryParams.schedulingStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择值班开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="值班结束时间" prop="schedulingEndTime">
        <el-date-picker clearable
          v-model="queryParams.schedulingEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择值班结束时间">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:scheduling:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:scheduling:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="schedulingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="值班人姓名" align="center" prop="sysUser.nickName" />
      <el-table-column label="值班人科室" align="center" prop="pSection.deptName" />
      <el-table-column label="值班人诊室" align="center" prop="pOutpatien.roomName" />
      <el-table-column label="值班开始时间" align="center" prop="schedulingStartTime" width="180" />
      <el-table-column label="值班结束时间" align="center" prop="schedulingEndTime" width="180" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:scheduling:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:scheduling:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改医生排班信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="值班人ID" prop="schedulingId">
          <el-select v-model="form.schedulingId" placeholder="请选择">
              <el-option
                v-for="item in userOptions"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId">
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="值班开始时间" prop="schedulingStartTime">
          <el-date-picker clearable
            v-model="form.schedulingStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择值班开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="值班结束时间" prop="schedulingEndTime">
          <el-date-picker clearable
            v-model="form.schedulingEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择值班结束时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listScheduling, getScheduling, delScheduling, addScheduling, updateScheduling } from "@/api/system/scheduling";
import { listUser } from "@/api/system/user";

export default {
  name: "Scheduling",
  data() {
    return {
      userOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 医生排班信息表格数据
      schedulingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schedulingId: null,
        keyWord1: null,
        schedulingStartTime: null,
        schedulingEndTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        schedulingId: [
          { required: true, message: "值班人ID不能为空", trigger: "blur" }
        ],
        schedulingStartTime: [
          { required: true, message: "值班开始时间不能为空", trigger: "blur" }
        ],
        schedulingEndTime: [
          { required: true, message: "值班结束时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getUserList()
  },
  methods: {
    getUserList(){
      let obj = Object.assign({
        pageNum: 1,
        pageSize: 10,
        type: '1',
      })
      listUser(obj).then(response => {
          this.userOptions = response.rows;
        }
      );
    },
    /** 查询医生排班信息列表 */
    getList() {
      this.loading = true;
      listScheduling(this.queryParams).then(response => {
        this.schedulingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        schedulingId: null,
        schedulingStartTime: null,
        schedulingEndTime: null,
        createTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加医生排班信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getScheduling(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改医生排班信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateScheduling(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addScheduling(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除医生排班信息编号为"' + ids + '"的数据项？').then(function() {
        return delScheduling(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/scheduling/export', {
        ...this.queryParams
      }, `scheduling_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
