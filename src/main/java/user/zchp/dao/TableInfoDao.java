package user.zchp.dao;import user.zchp.utils.PageParam;import javax.annotation.Resource;import java.util.HashMap;import java.util.List;@Resourcepublic interface TableInfoDao extends Dao {	List<HashMap> queryHashMap(PageParam pageParam);}