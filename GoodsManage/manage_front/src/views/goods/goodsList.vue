<template>
  <div>
    <div style="margin-top: 25px; margin-left: 20px">
      <el-tag type="info">物资查询:</el-tag>
      <el-input v-model="goodsNameInput" placeholder="请输入查询物资名称" style="width: 200px;margin-left: 8px"></el-input>
      <el-select v-model="selectedType" placeholder="物资类别" style="margin-left: 10px; width: 120px">
        <el-option
          v-for="(type, index) in goodsTypes"
          :key="index"
          :label="type"
          :value="type">
        </el-option>
      </el-select>
      <el-button icon="el-icon-search" style="margin-left: 10px" @click="clearSlots">清空</el-button>
      <el-button icon="el-icon-search" type="primary" style="margin-left: 15px" @click="goodsSearch">搜索 / 获取</el-button>
    </div>
    <div>
      <el-table
        :data="goodsTableData"
        border
        style="width: 98%; margin-left: 20px; margin-top: 30px">
        <el-table-column prop="goodsId" label="物资ID" v-if="false"></el-table-column>
        <el-table-column
          prop="goodsName"
          label="物资名称"
          sortable>
        </el-table-column>
        <el-table-column
          prop="goodsType"
          label="物资类别">
        </el-table-column>
        <el-table-column prop="picUrl" label="物资图片">
          <template slot-scope="scope">
            <img :src="scope.row.picUrl" style="width: 50px; height: 50px" alt="图片加载失败">
          </template>
        </el-table-column>
        <el-table-column
          prop="goodsDescription"
          label="物资描述">
        </el-table-column>
        <el-table-column
          prop="currentQuantity"
          label="目前在库数量"
          width="129px"
          sortable>
        </el-table-column>
        <el-table-column
          prop="quantityMark"
          label="库存预警值"
          width="115px"
          sortable>
        </el-table-column>
        <el-table-column prop="operation" label="操作">
          <template slot-scope="scope">
            <div>
              <el-button type="primary" size="mini" >查看编辑</el-button>
              <el-button type="danger" size="mini" style="margin-left: 20px" @click="openDeleteConfirmDialog(scope.row.goodsId, scope.row.goodsName)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="position: fixed; bottom: 10px; width: 100%; margin-left: 450px">
      <el-pagination
        @current-change="handlePageChange"
        background
        layout="prev, pager, next, total"
        :current-page.sync="pageNum"
        :page-size="pageSize"
        :page-count="pages"
        :total="total">
      </el-pagination>
    </div>
  <el-dialog
    title="确认删除"
    :visible.sync="deleteConfirmDialogVisible"
    width="30%"
    @close="deleteConfirmDialogVisible = false" :append-to-body="true" :center="true">
    <span>确定要删除这个物资: {{goodsNameToDelete}} 吗？</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="deleteConfirmDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="deleteGoods">确定</el-button>
    </span>
  </el-dialog>
  </div>
</template>
<script>
import { goodsList, goodsType, goodsSearch, deleteGoodsById } from "@/api/goods";
export default {
  created() {
    this.fetchGoodslist();
    this.fetchGoodsTypes();
  },
  data() {
    return {
      goodsNameInput: '',
      goodsTypes: [],
      selectedType: '',
      goodsTableData:[],

      total: 0,
      pageNum: 1,
      pageSize: 8,
      pages:0,

      deleteConfirmDialogVisible: false,
      goodsIdToDelete: null,
      goodsNameToDelete: null
    }
  },
  methods: {
    handlePageChange(val) {
      this.pageNum = val;
      this.fetchGoodslist();
    },
    fetchGoodslist() {
      console.log("fetch data of goods!");
      goodsList(this.pageNum, this.pageSize).then(res => {
        console.log(res.data);
        this.goodsTableData = res.data.goods.records
        this.total = res.data.goods.total
        this.pages = res.data.goods.pages
      })
    },
    fetchGoodsTypes() {
      console.log("fetch goods types!");
      goodsType().then(res => {
        console.log(res.data);
        this.goodsTypes = res.data.goodsTypes;
      })
    },
    goodsSearch(){
      console.log("search goods!");
      goodsSearch(this.goodsNameInput, this.selectedType, this.pageNum, this.pageSize).then(res => {
        console.log(res.data);
        this.goodsTableData = res.data.goods.records
        this.total = res.data.goods.total
        this.pages = res.data.goods.pages
      })
    },
    clearSlots() {
      this.selectedType = '';
      this.goodsNameInput = '';
      this.fetchGoodslist();
      this.fetchGoodsTypes();
    },
    openDeleteConfirmDialog(goodsId, goodName) {
      this.deleteConfirmDialogVisible = true;
      this.goodsIdToDelete = goodsId;
      this.goodsNameToDelete = goodName;
    },
    async deleteGoods() {
      try {
        let GoodsToDelete = { goodsId: this.goodsIdToDelete};
        console.log(this.goodsIdToDelete);
        await deleteGoodsById(GoodsToDelete);
        this.fetchGoodslist();
        this.fetchGoodsTypes();
        this.$message({
          message: "删除成功",
          type: "success",
        });
      } catch (err) {
        this.$message({
          message: "删除失败",
          type: "error",
        });
      } finally {
        this.deleteConfirmDialogVisible = false;
      }
    }
  }
}
</script>

<style scoped lang="scss">

</style>

