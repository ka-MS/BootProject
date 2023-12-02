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
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})// select 는 query insert,create,update 는 update 를 통해 호출 된다.
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs(); // 1번요소에는 파라미터로 넘어온 객체가 담겨있음
        String methodName = ((MappedStatement) args[0]).getSqlCommandType().name();// 해당요소에 실제 실행 메소드명 담겨있음. 대문자임
        Object param = args[1]; // 파라미터로 넘어온 객체

        if(param instanceof BaseEntity){ // 객체 타입 확인
            BaseEntity entity = (BaseEntity)param; // 엔티티는 baseentity를 상속 받기 때문에 형변환
            if("INSERT".equals(methodName)){
                entity.interceptCreatedAt();
            }else if("UPDATE".equals(methodName)){
                entity.interceptUpdatedAt();
            }
        }

        return invocation.proceed();
    }
}
