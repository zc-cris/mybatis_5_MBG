package com.zc.cris.mybatis.test;                                                                                                         
                                                                                                                                          
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.zc.cris.mybatis.bean.Employee;
import com.zc.cris.mybatis.bean.EmployeeExample;
import com.zc.cris.mybatis.bean.EmployeeExample.Criteria;
import com.zc.cris.mybatis.dao.EmployeeMapper;                                                                                                        
                                                                                                                                          
class TestMybatis{                                                                                                                       
                                                                                                                                          
	public SqlSessionFactory getSqlSessionFactory() throws IOException {                                                                  
		String resource = "mybatis-config.xml";                                                                                           
		InputStream inputStream = Resources.getResourceAsStream(resource);                                                                
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);                                          
		return sqlSessionFactory;                                                                                                         
	}                                                                                                                                     
	public SqlSession getSession() throws IOException {                                                                                   
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();                                                                     
		SqlSession session = sqlSessionFactory.openSession();                                                                             
		return session;                                                                                                                   
	}    
	
	@Test
	void testMBG() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
		  List<String> warnings = new ArrayList<String>();
		   boolean overwrite = true;
		   File configFile = new File("mbg.xml");
		   ConfigurationParser cp = new ConfigurationParser(warnings);
		   Configuration config = cp.parseConfiguration(configFile);
		   DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		   MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		   myBatisGenerator.generate(null);
	}

	
	/**
	 * 
	 * @MethodName: testMybatis3SelectById
	 * @Description: TODO (使用MBG生成的mapper接口和映射文件测试查询根据id查询)
	 * @throws IOException
	 * @Return Type: void
	 * @Author: zc-cris
	 */
	@Test
	void testMybatis3SelectById() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee emp = mapper.selectByPrimaryKey(1);
		System.out.println(emp);
	}
	
	/**
	 * 
	 * @MethodName: testMybatis3SelectAll
	 * @Description: TODO (测试炫酷的复杂查询，根据查询条件查询用户)
	 * @throws IOException
	 * @Return Type: void
	 * @Author: zc-cris
	 */
	@Test
	void testMybatis3SelectAll() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		EmployeeExample employeeExample = new EmployeeExample();
		
		// 使用employeeExample 的内部类Criteria 进行复杂查询条件的生成
		// 查询名字中含有字母c并且gender为0 或者 email还有@qq字母的所有用户,按照id的降序排序
		employeeExample.setDistinct(false);
		employeeExample.setOrderByClause("id desc");
		Criteria criteria = employeeExample.createCriteria();
		criteria.andNameLike("%c%");
		criteria.andGenderEqualTo("0");
		
		Criteria criteria2 = employeeExample.createCriteria();
		criteria2.andEmailLike("%@qq%");
		
		employeeExample.or(criteria2);
		
		List<Employee> emps = mapper.selectByExample(employeeExample);
		for (Employee employee : emps) {
			System.out.println(employee);
		}
		
		
		// 使用 employeeExample 的内部类Criteria 进行复杂查询条件的生成:查询名字为cris 的所有用户并且按照id降序排序
//		employeeExample.setDistinct(false);
//		employeeExample.setOrderByClause("id desc");
//		Criteria criteria = employeeExample.createCriteria();
//		criteria.andNameEqualTo("cris");
//		List<Employee> emps = mapper.selectByExample(employeeExample);
//		for (Employee employee : emps) {
//			System.out.println(employee);
//		}
		
		// 使用 employeeExample 进行查询处理 ，例如将查询的结果根据dept_id的降序排序,排序依据为数据表的列名
//		employeeExample.setOrderByClause("dept_id desc");
//		List<Employee> list = mapper.selectByExample(employeeExample);
//		for (Employee employee : list) {
//			System.out.println(employee);
//		}
		
		// 没有任何查询条件的查询全部员工
//		List<Employee> list = mapper.selectByExample(null);
//		for (Employee employee : list) {
//			System.out.println(employee);
//		}
	}
	
	/**
	 * 
	 * @MethodName: testUpdate
	 * @Description: TODO (测试数据增加)
	 * @throws IOException
	 * @Return Type: void
	 * @Author: zc-cris
	 */
	@Test
	void testUpdate() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee employee = new Employee();
		employee.setName("疯狂的麦克斯");
		employee.setGender("1");
		mapper.insert(employee);
		session.commit();
	}
	
}                                                                                                                                         
                                                                                                                                          