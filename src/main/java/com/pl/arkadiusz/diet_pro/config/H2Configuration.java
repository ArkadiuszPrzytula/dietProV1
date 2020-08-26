package com.pl.arkadiusz.diet_pro.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2Configuration {

    /**
     * Open the TPC port for the H2 database, so it is available remotely.
     * @return Server
     * @throws SQLException
     */

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return  Server.createTcpServer("-tcp");
    }
}
