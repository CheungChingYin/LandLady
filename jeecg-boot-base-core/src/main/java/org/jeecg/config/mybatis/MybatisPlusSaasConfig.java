package org.jeecg.config.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author zhoujf
 *
 */
@Configuration
@MapperScan(value={"org.jeecg.modules.**.mapper*","com.landlady.modules.**.mapper*"})
public class MybatisPlusSaasConfig {
    /**
     * tenant_id 字段名
     */
    private static final String TENANT_FIELD_NAME = "tenant_id";
    /**
     * 哪些表需要做多租户 表需要添加一个字段 tenant_id
     */
    @Value("${landlady.saas.ignoreTables}")
    private String ignoreTables;

    // 忽略表的前缀
    private static final List<String> ignoreTablePrefixList = new ArrayList<>();

    @Value("${landlady.saas.ignoreTablePrefix}")
    public void setIgnoreTablePrefixList(String ignoreTablePrefix) {
        if (StringUtils.isNoneBlank(ignoreTablePrefix)) {
            ignoreTablePrefixList.addAll(Arrays.asList(ignoreTablePrefix.split(";")));
        }
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String tenantId = oConvertUtils.getString(TenantContext.getTenant(),"0");
                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn(){
                return TENANT_FIELD_NAME;
            }

            /**
             * 检查当前查询表是否需要走多租户模式
             * 适用指定表名以及表名前缀
             *
             * @param tableName
             * @return 返回 true 表示不走租户逻辑
             */
            @Override
            public boolean ignoreTable(String tableName) {
                // 如果忽略租户表名中含有当前的表名，则不走租户逻辑
                if (StringUtils.isNoneBlank(ignoreTables)) {
                    if (ignoreTables.contains(tableName)) {
                        return true;
                    }
                }
                // 判断表开头是否符合忽略条件
                for (String tablePrefix : ignoreTablePrefixList) {
                    if (tableName.toLowerCase().startsWith(tablePrefix.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //update-begin-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor());
        //update-end-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        return interceptor;
    }

    /**
     * 动态表名切换拦截器,用于适配vue2和vue3同一个表有多个的情况,如sys_role_index在vue3情况下表名为sys_role_index_v3
     * @return
     */
    private DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            //获取需要动态解析的表名
            String dynamicTableName = ThreadLocalDataHelper.get(CommonConstant.DYNAMIC_TABLE_NAME);
            //当dynamicTableName不为空时才走动态表名处理逻辑,否则返回原始表名
            if (ObjectUtil.isNotEmpty(dynamicTableName) && dynamicTableName.equals(tableName)) {
                // 获取前端传递的版本号标识
                Object version = ThreadLocalDataHelper.get(CommonConstant.VERSION);
                if (ObjectUtil.isNotEmpty(version)) {
                    //拼接表名规则(原始表名+下划线+前端传递的版本号)
                    return tableName + "_" + version;
                }
            }
            return tableName;
        });
        return dynamicTableNameInnerInterceptor;
    }

//    /**
//     * 下个版本会删除，现在为了避免缓存出现问题不得不配置
//     * @return
//     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

}
