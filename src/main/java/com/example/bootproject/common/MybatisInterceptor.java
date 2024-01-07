package com.example.bootproject.common;

import com.example.bootproject.post.domain.BaseEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //Element 1 contains the object passed as a parameter.
        Object[] args = invocation.getArgs();
        // The element contains the actual execution method name in capital letters.
        String methodName = ((MappedStatement) args[0]).getSqlCommandType().name();
        Object param = args[1];

        // Check Object type
        if(param instanceof BaseEntity){
            // Type conversion because entities extends BaseEntity.
            BaseEntity entity = (BaseEntity)param;
            if("INSERT".equals(methodName)){
                entity.interceptCreatedAt();
            }else if("UPDATE".equals(methodName)){
                entity.interceptUpdatedAt();
            }
        }

        return invocation.proceed();
    }
}
