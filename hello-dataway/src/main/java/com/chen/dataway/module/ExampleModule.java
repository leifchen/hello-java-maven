package com.chen.dataway.module;

import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


/**
 * 示例模块
 * <p>
 * @Author LeifChen
 * @Date 2020-05-13
 */
@DimModule
@Component
public class ExampleModule implements SpringModule {

    @Autowired
    private DataSource dataSource;

    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
    }
}
