package com.jewelry.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration // 자바 기반의 설정 파일로 인식
@PropertySource("classpath:/application.properties") // 해당 클래스에서 참조할 properties 파일의 위치를 지정
@EnableTransactionManagement
public class DatabaseConfig {

	@Autowired // 빈(Bean)으로 등록된 인스턴스(이하 객체)를 클래스에 주입하는 데 사용합니다. ( := @Resource, @Inject )
	private ApplicationContext applicationContext;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari") // spring.datasource.hikari로 시작하는 설정을 모두 읽기.
	public HikariConfig hikariConfig() { // Connection Pool 객체 생성
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	// Bean : name 속성 설정시 속성 값으로 호출 가능, 메서드명으로는 호출 불가능
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		// -> 패턴에 포함되는 XML Mapper를 인식하기 위함
		factoryBean.setTypeAliasesPackage("com.jewelry.*.domain.*");
		// -> XML Mapper의 parameterType, resultType를 인식하기 위한 타입 설정 (풀 패키지경로이며 복잡한 경우
		// 애스터리스크 사용 가능 ex) com.myapp.board.*.domain
		factoryBean.setConfiguration(mybatisConfg());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}

	@Bean
	public TransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
