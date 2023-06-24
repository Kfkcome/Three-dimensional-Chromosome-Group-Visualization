# Three-dimensional-Chromosome-Group-Visualization
Three-dimensional Chromosome Group Visualization
## 技术栈 
1. springboot 
2. mybatis
3. mysql
## 数据来源
1. A2_Compartment.bed
2. A2_loop.txt
3. A2_matrix.hic(使用straw包来解析)
4. A2_TAD.bed
5. A2_gene.gff3 （存储了染色体结构）
除hic文件外其他文件全部转换到mysql数据库存储
## 依赖 
### java-straw
[java-straw](https://github.com/sa501428/java-straw)
用来解析.hic文件
