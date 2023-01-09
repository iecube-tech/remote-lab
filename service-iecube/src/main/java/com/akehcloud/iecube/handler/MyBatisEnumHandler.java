package com.akehcloud.iecube.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@MappedTypes({Enum.class})
public class MyBatisEnumHandler<T extends Enum<T>> implements TypeHandler<Enum<T>> {

    private final Class<T> clazz;

    public MyBatisEnumHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Enum<T> tEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, tEnum.name());
    }

    @Override
    public Enum<T> getResult(ResultSet resultSet, String s) throws SQLException {
        return Enum.valueOf(clazz, resultSet.getString(s));
    }

    @Override
    public Enum<T> getResult(ResultSet resultSet, int i) throws SQLException {
        return Enum.valueOf(clazz, resultSet.getString(i));
    }

    @Override
    public Enum<T> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return Enum.valueOf(clazz, callableStatement.getString(i));
    }

}
