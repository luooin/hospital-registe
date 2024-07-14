<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="预约日期" prop="registrationTime">
        <el-date-picker clearable
          v-model="queryParams.registrationTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预约日期">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:registration:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:registration:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="registrationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="就诊人" align="center" prop="sysUser.nickName" />
      <el-table-column label="预约日期" align="center" prop="registrationTime" width="180" />
      <el-table-column label="就诊状态" align="center" prop="status" >
        <template slot-scope="scope">
          {{scope.row.status === '0' ? '待就诊' : scope.row.status === '3' ? '已评价' :scope.row.status === '1' ? '已取消' : '已完成' }}
        </template>
      </el-table-column>
      <el-table-column label="就诊医生" align="center" prop="doctorName" />
      <el-table-column label="就诊科室" align="center" prop="pSection.deptName" />
      <el-table-column label="就诊诊室" align="center" prop="pOutpatien.roomName" />
      <el-table-column label="取消时间" align="center" prop="cancelTime" width="180" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:registration:edit']"
          >修改</el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleAccomplish(scope.row)"
            v-if="scope.row.status === '0'"
            v-hasPermi="['system:registration:edit']"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:registration:remove']"
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

    <!-- 添加或修改预约挂号记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="就诊人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入就诊人ID" />
        </el-form-item>
        <el-form-item label="预约日期" prop="registrationTime">
          <el-date-picker clearable
            v-model="form.registrationTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预约日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="就诊医生" prop="doctorId">
          <el-input v-model="form.doctorId" placeholder="请输入就诊医生" />
        </el-form-item>
        <el-form-item label="就诊科室" prop="sectionId">
          <el-input v-model="form.sectionId" placeholder="请输入就诊科室" />
        </el-form-item>
        <el-form-item label="就诊诊室" prop="outpatien">
          <el-input v-model="form.outpatien" placeholder="请输入就诊诊室" />
        </el-form-item>
        <el-form-item label="取消时间" prop="cancelTime">
          <el-date-picker clearable
            v-model="form.cancelTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择取消时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listRegistration, getRegistration, delRegistration, addRegistration, updateRegistration } from "@/api/system/registration";

export default {
  name: "Registration",
  data() {
    return {
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
      // 预约挂号记录表格数据
      registrationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        registrationTime: null,
        status: null,
        doctorId: null,
        sectionId: null,
        outpatien: null,
        cancelTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "就诊人ID不能为空", trigger: "blur" }
        ],
        registrationTime: [
          { required: true, message: "预约日期不能为空", trigger: "blur" }
        ],
        doctorId: [
          { required: true, message: "就诊医生不能为空", trigger: "blur" }
        ],
        sectionId: [
          { required: true, message: "就诊科室不能为空", trigger: "blur" }
        ],
        outpatien: [
          { required: true, message: "就诊诊室不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询预约挂号记录列表 */
    getList() {
      this.loading = true;
      listRegistration(this.queryParams).then(response => {
        this.registrationList = response.rows;
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
        orderNo: null,
        userId: null,
        registrationTime: null,
        createTime: null,
        status: null,
        doctorId: null,
        sectionId: null,
        outpatien: null,
        cancelTime: null,
        remark: null
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
      this.title = "添加预约挂号记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRegistration(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改预约挂号记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRegistration(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRegistration(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    
    /** 完成按钮操作 */
    handleAccomplish(row) {
      const ids = row.id;
      let obj = Object.assign({
        id: row.id,
        status: '2'
      })
      this.$modal.confirm('是否确认完成预约挂号记录编号为"' + row.id + '"的数据项？').then(function() {
        return updateRegistration(obj);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除预约挂号记录编号为"' + ids + '"的数据项？').then(function() {
        return delRegistration(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/registration/export', {
        ...this.queryParams
      }, `registration_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
