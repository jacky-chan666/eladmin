<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">申请单UUID</label>
        <el-input v-model="query.applicantId" clearable placeholder="申请单UUID" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">申请人姓名</label>
        <el-input v-model="query.applicantName" clearable placeholder="申请人姓名" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">申请单类型：新增，修改，上线，下线</label>
        <el-input v-model="query.applicationType" clearable placeholder="申请单类型：新增，修改，上线，下线" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">申请单数据类型：omada，vigi，adblocking</label>
        <el-input v-model="query.applicationDataType" clearable placeholder="申请单数据类型：omada，vigi，adblocking" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">申请单标题</label>
        <el-input v-model="query.applicationTitle" clearable placeholder="申请单标题" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="申请状态" prop="status">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="applicantId" label="申请单UUID" />
        <el-table-column prop="applicantName" label="申请人姓名" />
        <el-table-column prop="applicationDate" label="申请日期" />
        <el-table-column prop="applicationType" label="申请单类型：新增，修改，上线，下线" />
        <el-table-column prop="applicationDataType" label="申请单数据类型：omada，vigi，adblocking" />
        <el-table-column prop="applicationTitle" label="申请单标题" />
        <el-table-column prop="status" label="申请状态" />
        <el-table-column v-if="checkPer(['admin','deviceApplicationForm:edit','deviceApplicationForm:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudDeviceApplicationForm from '@/api/deviceApplicationForm'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, applicantId: null, applicantName: null, department: null, applicationDate: null, applicationDataId: null, applicationType: null, applicationDataType: null, applicationTitle: null, applicationReason: null, status: null, testContact: null, testLeader: null, devContact: null, devLeader: null, testContactApproval: null, testLeaderApproval: null, devContactApproval: null, devLeaderApproval: null, testContactComment: null, testLeaderComment: null, devContactComment: null, devLeaderComment: null, currentApprovers: null, approvalHistory: null, createdAt: null, updatedAt: null }
export default {
  name: 'DeviceApplicationForm',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '审核接口', url: 'api/deviceApplicationForm', idField: 'id', sort: 'id,desc', crudMethod: { ...crudDeviceApplicationForm }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'deviceApplicationForm:add'],
        edit: ['admin', 'deviceApplicationForm:edit'],
        del: ['admin', 'deviceApplicationForm:del']
      },
      rules: {
        applicantId: [
          { required: true, message: '申请单UUID不能为空', trigger: 'blur' }
        ],
        applicantName: [
          { required: true, message: '申请人姓名不能为空', trigger: 'blur' }
        ],
        applicationDate: [
          { required: true, message: '申请日期不能为空', trigger: 'blur' }
        ],
        applicationType: [
          { required: true, message: '申请单类型：新增，修改，上线，下线不能为空', trigger: 'blur' }
        ],
        applicationDataType: [
          { required: true, message: '申请单数据类型：omada，vigi，adblocking不能为空', trigger: 'blur' }
        ],
        applicationTitle: [
          { required: true, message: '申请单标题不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '申请状态不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'applicantId', display_name: '申请单UUID' },
        { key: 'applicantName', display_name: '申请人姓名' },
        { key: 'applicationType', display_name: '申请单类型：新增，修改，上线，下线' },
        { key: 'applicationDataType', display_name: '申请单数据类型：omada，vigi，adblocking' },
        { key: 'applicationTitle', display_name: '申请单标题' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
